package com.dysy.bysj;

import com.dysy.bysj.common.Constants;
import com.dysy.bysj.entity.Menu;
import com.dysy.bysj.mapper.MenuMapper;
import com.dysy.bysj.service.MenuService;
import com.dysy.bysj.service.impl.MenuServiceImpl;
import com.dysy.bysj.util.CommonUtils;
import com.dysy.bysj.util.EncrypUtils;
import com.dysy.bysj.util.RedisUtils;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest
class BysjApplicationTests {

//	@Autowired
//	StringEncryptor encryptor;
//
//	@Autowired
//	EncrypUtils encrypUtils;
//
//	@Autowired
//	RedisUtils redisUtils;

	@Autowired
	MenuServiceImpl menuService;

	@Resource
	MenuMapper menuMapper;

//	@Test
//	void test2() {
//		String encrypt = encryptor.encrypt("123456q");
//		System.out.println(encrypt);
//	}
//
//	@Test
//	void contextLoads() {
//		String randomSequence = CommonUtils.getRandomSequence(Constants.RANDOM_SALT_LENGTH);
//		String pwd = encrypUtils.encryp(Constants.APP_ALGORITHM_NAME, 123456, randomSequence,
//				Constants.APP_SECRET_HASH_ITERATIONS);
//		System.out.println("randomSequence = " + randomSequence);
//		String encodeSalt = encrypUtils.base64Encode(randomSequence);
//		System.out.println("编码之后 = " + encodeSalt);
//		System.out.println("解码之后 = " + encrypUtils.base64Decode(encodeSalt));
//		System.out.println("pwd = " + pwd);
//	}
//
//	@Test
//	void test1() {
//		System.out.println(null instanceof Object);
//	}
//
//	@Test
//	void test3(){
//		boolean b = redisUtils.hasKey("123");
//		System.out.println(b);
//	}
	@Test
	void test5() {
//		Object list = Arrays.asList(1,2);
//		List<Long> result = (List<Long>)list;
//		System.out.println(result);
		ArrayList<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3,5,9));
		ArrayList<Integer> list2 = new ArrayList<>(Arrays.asList(1, 2, 3, 6));
//		boolean b = list.removeAll(list2);
//		System.out.println(list);

		List<Integer> addIds = list2.stream().filter(e -> !list.contains(e))
				.collect(Collectors.toList());
		list2.removeAll(addIds);
		list.removeAll(list2);
		System.out.println(addIds);
		System.out.println(list);
	}

	@Test
	void test6() {
		long start = System.currentTimeMillis();
		List<Long> allSubMenu = menuMapper.getSubMenuIds(1L);
		System.out.println(allSubMenu);
		System.out.println(System.currentTimeMillis() - start);
	}

}
