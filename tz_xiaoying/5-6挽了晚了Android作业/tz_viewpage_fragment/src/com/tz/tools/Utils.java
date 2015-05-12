package com.tz.tools;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.tz.bean.CdrMoc;
import com.tz.bean.Customer;

public class Utils {
	public static List<CdrMoc> getCdrMocs() {
		List<CdrMoc> list = new ArrayList<CdrMoc>();
		for (int i = 0; i <= 3; i++) {
			int calltype = (i == 2 ? 1 : 0);
			String phone = (i == 2 ? "15618519793" : "13" + i + "0124109" + i);
			CdrMoc cdr = new CdrMoc(calltype, new Date(), 10 + i);
			Customer c = new Customer("C" + i, "张某" + i, phone);
			c.setDialStatus(i);
			c.setIntent(i);
			cdr.setCustomer(c);
			list.add(cdr);
		}

		return list;
	}
}
