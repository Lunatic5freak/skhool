package com.ba.skhool.student.manager;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ba.skhool.iam.context.UserSessionContextHolder;
import com.ba.skhool.student.dto.ResponseDTO;
import com.ba.skhool.student.dto.SchoolClassDTO;
import com.ba.skhool.student.entity.ClassSubjectMap;
import com.ba.skhool.student.entity.SchoolClass;
import com.ba.skhool.student.repository.ClassSubjectRepository;
import com.ba.skhool.student.repository.SchoolClassRepository;

import jakarta.transaction.Transactional;

@Component
public class SchoolClassManager {

	@Autowired
	private SchoolClassRepository classRepo;

	@Autowired
	private ClassSubjectRepository classSubRepo;

	public List<SchoolClass> getAllClasses() {
		return classRepo.findAll();
	}

	@Transactional
	public ResponseDTO saveClasses(List<SchoolClassDTO> classDtos) {
		try {
			List<SchoolClass> classes = classDtos.stream().map(c -> {
				SchoolClass sc = new SchoolClass();
				BeanUtils.copyProperties(c, sc);
				sc.setCreatedBy(UserSessionContextHolder.getUsername());
				return sc;
			}).toList();
			classRepo.saveAll(classes);
			return ResponseDTO.builder().message("Classes saved").success(true).build();
		} catch (Exception e) {
			return ResponseDTO.builder().message("Error while saving classes").success(false).build();
		}
	}

	public List<ClassSubjectMap> getClassVsSubject(Long classId) {
		return classSubRepo.getByClassId_id(classId);
	}

}
