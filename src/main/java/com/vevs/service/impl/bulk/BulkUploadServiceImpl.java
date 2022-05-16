package com.vevs.service.impl.bulk;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.vevs.common.exception.VmsException;
import com.vevs.common.util.VMSUtils;
import com.vevs.controller.validator.BulkValidator;
import com.vevs.controller.validator.Validator;
import com.vevs.entity.bulk.BulkLocationRecords;
import com.vevs.entity.bulk.BulkUploadRecordsFile;
import com.vevs.entity.bulk.BulkUserRecords;
import com.vevs.entity.common.Location;
import com.vevs.entity.common.Role;
import com.vevs.entity.common.RoleName;
import com.vevs.entity.common.VMSEnum;
import com.vevs.entity.employee.Users;
import com.vevs.entity.virtualObject.BulkRejectVO;
import com.vevs.entity.virtualObject.UserVO;
import com.vevs.i18N.MessageByLocaleService;
import com.vevs.repo.LocationRepository;
import com.vevs.repo.RoleRepository;
import com.vevs.repo.UserRepository;
import com.vevs.repo.bulk.BulkLocationRecordsRepository;
import com.vevs.repo.bulk.BulkUploadRecordsFileRepository;
import com.vevs.repo.bulk.BulkUserRecordsRepository;
import com.vevs.service.BulkUploadService;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class BulkUploadServiceImpl extends BulkHelper implements BulkUploadService {

    @Autowired
    BulkUploadRecordsFileRepository fileUploadRepository;

    @Autowired
    BulkLocationRecordsRepository locationRecordsRepository;

    @Autowired
    BulkUserRecordsRepository userRecordsRepository;

    @Autowired
    BulkValidator bulkValidator;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    MessageByLocaleService messageSource;

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    private BulkLocationRecordsRepository bulkLocationRecordsRepository;

    @Autowired
    Validator validator;

    @Override
    public BulkUploadRecordsFile uploadNewFileData(Long userId, MultipartFile file, String fileName, String module) throws Exception {

        Timestamp currentTime = VMSUtils.currentTime();
        List<String[]> fileDataList = bulkValidator.fetchFileDataRowWise(file);
        String[] headerColumns = fileDataList.remove(0);
        List<BulkLocationRecords> bulkLocationList = new ArrayList<>();
        List<BulkUserRecords> bulkUserList = new ArrayList<>();
        int listSize = 0;
        if (StringUtils.equalsIgnoreCase(module, VMSEnum.MODULE_NAME.LOCATION.name())) {
            bulkLocationList = locationRecordFromFile(fileDataList, headerColumns);
            listSize = bulkLocationList.size();
        } else {
            bulkUserList = userRecordFromFile(fileDataList, headerColumns);
            listSize = bulkUserList.size();
        }
        String status = VMSEnum.BULK_UPLOAD_STATUS.PENDING.name();
        BulkUploadRecordsFile uploadRecordsFile = new BulkUploadRecordsFile();
        uploadRecordsFile.setStatus(status);
        uploadRecordsFile.setFileName(fileName);
        uploadRecordsFile.setCreatedBy(userId);
        uploadRecordsFile.setCreatedOn(currentTime);
        uploadRecordsFile.setNoOfRecords(listSize);
        uploadRecordsFile.setModule(module.toUpperCase());
        BulkUploadRecordsFile saveBulkRecord = fileUploadRepository.save(uploadRecordsFile);

        Long bulkUploadFileId = saveBulkRecord.getId();
        if (StringUtils.equalsIgnoreCase(module, VMSEnum.MODULE_NAME.LOCATION.name())) {
            for (BulkLocationRecords record : bulkLocationList) {
                record.setUploadedFileId(bulkUploadFileId);
            }
            locationRecordsRepository.saveAll(bulkLocationList);
        } else {
            for (BulkUserRecords record : bulkUserList) {
                record.setUploadedFileId(saveBulkRecord.getId());
            }
            userRecordsRepository.saveAll(bulkUserList);
        }
        return saveBulkRecord;
    }

    @Override
    public BulkUploadRecordsFile validateFileRecord(Long uploadedFileId, Long loggedInUserId) {
        Timestamp currentTime = VMSUtils.currentTime();
        BulkUploadRecordsFile record = fileUploadRepository.getById(uploadedFileId);
        return updateBulkRecordsAsValidate(record, loggedInUserId, currentTime);
    }

    @Override
    public BulkUploadRecordsFile rejectFileRecord(BulkRejectVO rejectVO, Long userId) {
        Timestamp now = VMSUtils.currentTime();
        Long uploadedFileId = rejectVO.getUploadedFileId();
        BulkUploadRecordsFile record = fileUploadRepository.getById(uploadedFileId);
        String rejectReason = rejectVO.getRejectReason();
        return updateBulkRecordsAsReject(record, userId, now, rejectReason);

    }

    @Override
    public BulkUploadRecordsFile submitFileRecord(Long uploadedFileId, Long loggedInUserId) {
        Timestamp currentTime = VMSUtils.currentTime();
        BulkUploadRecordsFile record = fileUploadRepository.getById(uploadedFileId);
        String module = record.getModule();
        if (StringUtils.equalsIgnoreCase(module, VMSEnum.MODULE_NAME.LOCATION.name())) {
            addNewLocations(uploadedFileId, loggedInUserId, currentTime);
        } else {
            addNewUsers(uploadedFileId, loggedInUserId, currentTime);
        }
        return updateBulkRecordsAsSubmitted(record, loggedInUserId, currentTime);
    }

    @Override
    public List<String> bulkUploadModuleNames() {
        return Stream.of(VMSEnum.MODULE_NAME.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> bulkUploadStatus() {
        return Stream.of(VMSEnum.BULK_UPLOAD_STATUS.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }


    protected BulkUploadRecordsFile updateBulkRecordsAsSubmitted(BulkUploadRecordsFile record, Long userId, Timestamp now) {
        markAsModified(record, userId, now);
        markAsSubmittedFileRecord(record, userId, now);
        return fileUploadRepository.save(record);
    }

    protected BulkUploadRecordsFile updateBulkRecordsAsValidate(BulkUploadRecordsFile record, Long userId, Timestamp now) {
        markAsModified(record, userId, now);
        String moduleName = record.getModule();
        List<String> issuesList = new ArrayList<>();
        if (StringUtils.equalsIgnoreCase(moduleName, VMSEnum.MODULE_NAME.LOCATION.name())) {
            validateLocationRecordOfFile(issuesList,record.getId());
        } else {
            validateUserRecordOfFile(issuesList,record.getId());
        }
        if(!issuesList.isEmpty()){
            markAsValidateFileRecordFailed(issuesList,record, userId, now);
        }else {
            markAsValidateFileRecordSuccess(record, userId, now);
        }
        return fileUploadRepository.save(record);
    }

    private List<String> validateUserRecordOfFile(List<String> issuesList, Long id) {
        List<BulkUserRecords> usersRecords = userRecordsRepository.findAllByUploadedFileId(id);
        for (BulkUserRecords userFromFile : usersRecords) {
            UserVO newUser = new UserVO();

            newUser.setName(userFromFile.getName());
            newUser.setEmail(userFromFile.getEmail());
            newUser.setUsername(userFromFile.getUserName());
            newUser.setMobileNo(userFromFile.getMobileNumber());
            newUser.setDesignation(userFromFile.getDesignation());
            newUser.setEmployeeCode(userFromFile.getEmployeeCode());
            newUser.setPassword("Anuj@Kumar1");
            newUser.setRole((userFromFile.getRole()).toUpperCase());
            issuesList.addAll(validator.validateNewUser(newUser));
        }

        return issuesList;
    }


    private List<String> validateLocationRecordOfFile(List<String> issuesList,Long id) {

        List<BulkLocationRecords> locationRecords = bulkLocationRecordsRepository.findAllByUploadedFileId(id);
        for (BulkLocationRecords locationFromFile : locationRecords) {
            Location newLocation = new Location();
            newLocation.setName(locationFromFile.getLocationName());
            newLocation.setLocationAddress(locationFromFile.getAddress());
            newLocation.setLocationContactNo(locationFromFile.getContactNumber());
            newLocation.setCountry(locationFromFile.getCountry());
            issuesList.addAll(validator.createLocation(newLocation));
        }
        return issuesList;
    }


    protected BulkUploadRecordsFile updateBulkRecordsAsReject(BulkUploadRecordsFile record, Long userId, Timestamp now, String rejectReason) {

        markAsModified(record, userId, now);
        markAsRejectFileRecord(record, userId, now);
        record.setRejectReason(rejectReason);
        return fileUploadRepository.save(record);
    }

    private void addNewUsers(Long uploadedFileId, Long loggedInUserId, Timestamp currentTime) {
        List<Users> usersList = new ArrayList<Users>();
        List<BulkUserRecords> userRecords = userRecordsRepository.findAllByUploadedFileId(uploadedFileId);
        for (BulkUserRecords userFromFile : userRecords) {
            Users newUser = new Users();
            newUser.setEnable(true);
            newUser.setName(userFromFile.getName());
            newUser.setEmail(userFromFile.getEmail());
            newUser.setUsername(userFromFile.getUserName());
            newUser.setMobileNo(userFromFile.getMobileNumber());
            newUser.setDesignation(userFromFile.getDesignation());
            newUser.setEmployeeCode(userFromFile.getEmployeeCode());
            Role role = getUserRole(userFromFile.getRole());
            newUser.setRoles(Collections.singleton(role));
            newUser.setCreatedBy(loggedInUserId);
            newUser.setCreatedOn(currentTime);
            newUser.setPassword(passwordEncoder.encode(generatePassword()));
            newUser.setBaseLocation(locationRepository.getById(userFromFile.getBaseLocationId()));

            usersList.add(newUser);
        }
        userRepository.saveAll(usersList);
    }

    private String generatePassword() {
        return "admin";
    }


    Role getUserRole(String roleName) {
        roleName = roleName.toUpperCase();
        Role userRole = null;
        if (StringUtils.equals(roleName, RoleName.USER.name())) {
            userRole = roleRepository.findByName(RoleName.USER)
                    .orElseThrow(() -> new VmsException(messageSource.getMessage("user.error.role.not.set")));
        } else if (StringUtils.equals(roleName, RoleName.ADMIN.name())) {
            userRole = roleRepository.findByName(RoleName.ADMIN)
                    .orElseThrow(() -> new VmsException(messageSource.getMessage("user.error.role.not.set")));
        } else if (StringUtils.equals(roleName, RoleName.FRONT_DESK.name())) {
            userRole = roleRepository.findByName(RoleName.FRONT_DESK)
                    .orElseThrow(() -> new VmsException(messageSource.getMessage("user.error.role.not.set")));
        }
        return userRole;
    }


    private void addNewLocations(Long uploadedFileId, Long loggedInUserId, Timestamp currentTime) {

        List<Location> newLocationList = new ArrayList<Location>();
        List<BulkLocationRecords> locationRecords = locationRecordsRepository.findAllByUploadedFileId(uploadedFileId);
        for (BulkLocationRecords location : locationRecords) {
            Location newLocation = new Location();
            newLocation.setName(location.getLocationName());
            newLocation.setLocationAddress(location.getAddress());
            newLocation.setLocationContactNo(location.getContactNumber());
            newLocation.setCountry(location.getCountry());
            newLocation.setCreatedBy(loggedInUserId);
            newLocation.setEnable(true);
            newLocation.setCreatedOn(currentTime);
            newLocationList.add(newLocation);
        }
        locationRepository.saveAll(newLocationList);
    }


}
