package com.ba.skhool.student.manager;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ba.skhool.iam.context.UserSessionContextHolder;
import com.ba.skhool.iam.repository.SubjectRepository;
import com.ba.skhool.student.dto.SubjectRequest;
import com.ba.skhool.student.entity.Subject;

import jakarta.transaction.Transactional;

@Component
public class SubjectManager {

	private Logger LOGGER = LoggerFactory.getLogger(SubjectManager.class);

	@Autowired
	private SubjectRepository subjectRepo;

	@Transactional
	public Subject createSubject(SubjectRequest request) {
		try {
			Subject s = new Subject();
			BeanUtils.copyProperties(request, s);
			s.setOrganization(UserSessionContextHolder.getTenantId());
			s.setCreatedBy(UserSessionContextHolder.getUsername());
			return subjectRepo.save(s);
		} catch (Exception e) {
			LOGGER.error("Error in creating Subject: {}", e);
			return null;
		}
	}

	public List<Subject> getSubjects() {
		return subjectRepo.findAll();
	}

	public Subject findById(Long subjectId) {
		return subjectRepo.findById(subjectId).orElse(null);
	}

	public Map<Long, Subject> findByIds(List<Long> ids) {
		return subjectRepo.findAllById(ids).stream().collect(Collectors.toMap(Subject::getId, Function.identity()));
	}

}
