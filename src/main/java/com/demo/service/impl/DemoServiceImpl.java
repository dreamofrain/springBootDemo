package com.demo.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.entity.Demo;
import com.demo.repository.DemoRepository;
import com.demo.service.DemoService;

@Service
@Transactional
public class DemoServiceImpl implements DemoService {
	@Autowired
	private DemoRepository demoRepository;

	@Override
	public Demo save(Demo demo) {
		return demoRepository.save(demo);
	}

	@Override
	public void deleteDemo(int id) {
		demoRepository.delete(id);
	}

	// @Override
	// public Demo getById(Integer id) {
	// return demoRepository.findOne(id);
	// }

	@Override
	public Map<String, Object> getPackage(int pageSize, int page, String otherValue) {
		// 根据ID排序
		Sort sort = new Sort(Sort.Direction.ASC, "id");
		// 分页类
		Pageable pageable = new PageRequest(pageSize, page, sort);
		// 高级查询
		Specification<Demo> querySpecifi = new Specification<Demo>() {
			@Override
			public Predicate toPredicate(Root<Demo> root, CriteriaQuery<?> criteriaQuery,
					CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<>();

				// 拼接查询参数
				if (otherValue != null && !("").equals(otherValue)) {
					predicates.add(criteriaBuilder.like(root.get("type"), otherValue));
				}

				predicates.add(criteriaBuilder.equal(root.get("status"), "无"));

				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
		// 获取查询结果
		Page<Demo> demos = demoRepository.findAll(querySpecifi, pageable);
		// 返回一个map用于前台分页
		Map<String, Object> result = new HashMap<String, Object>();

		result.put("total", demos.getTotalElements());
		result.put("rows", demos.getContent());

		return result;
	}

	@Override
	public List<Demo> getDemos() {
		return demoRepository.findDemos();
	}

	@Override
	public int getNUM(String name) {
		return demoRepository.getNUMS(name);
	}

	@Override
	public List<Demo> getNums(Specification<Demo> spec) {
		return demoRepository.findAll(spec);
	}

	@Override
	public void deleteAll() {
		demoRepository.deleteAll();
	}

	@Override
	public List<Demo> saveList(List<Demo> demos) {
		return (List<Demo>) demoRepository.save(demos);
	}

	@Override
	public List<Demo> getDemosByType(String type) {
		return demoRepository.getDemosByType(type);
	}

	@Override
	public int deleteDemoByType(String type) {
		return demoRepository.deleteDemoByType(type);
	}

	@Override
	public void generateMessage(Integer id, String status) {
		demoRepository.generateMessage(id, status);
	}

	@Override
	public Map<String, Object> getPackageMSG(int pageSize, int page, String status) {
		// 根据ID排序
		Sort sort = new Sort(Sort.Direction.ASC, "id");
		// 分页类
		Pageable pageable = new PageRequest(pageSize, page, sort);
		// 高级查询
		Specification<Demo> querySpecifi = new Specification<Demo>() {
			@Override
			public Predicate toPredicate(Root<Demo> root, CriteriaQuery<?> criteriaQuery,
					CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<>();
				predicates.add(criteriaBuilder.equal(root.get("status"), status));
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
		// 获取查询结果
		Page<Demo> demos = demoRepository.findAll(querySpecifi, pageable);
		// 返回一个map用于前台分页
		Map<String, Object> result = new HashMap<String, Object>();

		result.put("total", demos.getTotalElements());
		result.put("rows", demos.getContent());

		return result;
	}

	@Override
	public void returnMessage(String status) {
		demoRepository.returnMessage(status);
	}
}