package cn.com.esrichina.adapter.vcloud.test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
	public static void main(String[] args) {
		BigInteger big = new BigInteger("123");
		int v = big.intValue();
		System.out.println(v);
		Person p1 = new Person("one", 1);
		Person p2 = new Person("two", 2);
		Person p3 = new Person("three", 3);
		Person p4 = new Person("four", 4);
		Person p5 = new Person("five", 5);
		
		Person[] arrys = new Person[5];
		arrys[0] = p1;
		arrys[1] = p2;
		arrys[2] = p3;
		arrys[3] = p4;
		arrys[4] = p5;
		System.out.println("----------------------------------");
		for(Person a : arrys) {
			System.out.println(a);
		}
		
		List<Person> ps = new ArrayList<Person>();
		
		ps.addAll(Arrays.asList(arrys));
		
		
		for(Person p : ps) {
			System.out.println(p);
		}
		
//		Object[] array2 = ps.toArray();
//		
//		for(Object a : array2) {
//			System.out.println(a);
//		}
		Person[] pas = new Person[ps.size()];
		int i = 0;
		for(Person p : ps) {
			pas[i] = p;
			i++;
		}
		System.out.println("----------------------------------");
		for(Person p : pas) {
			System.out.println(p);
		}
	}

	
}
