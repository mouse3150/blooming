package cn.com.esrichina.adapter.aliyun.test;


public class AliyunRdsTest {

//	private static AliyunClient client;
//	@Before
//	public void setUp() throws Exception {
//		client = new DefaultAliyunClient(AliyunConstants.SERVER_URL, AliyunConstants.ACCESS_KEY_ID, AliyunConstants.ACCESS_KEY_SECRET);
//	}
//
//	@Test
//	public void testDescribeAccounts() {
//		DescribeAccountsRequest describeAccountsRequest = new DescribeAccountsRequest();
//		describeAccountsRequest.setdBInstanceId(AliyunConstants.DB_INSTANCE_ID);
//		AliyunUtils.setClient(client);
//		DescribeAccountsResponse response = AliyunUtils.execute(describeAccountsRequest);
//		List<DBInstanceAccount> accounts = response.getAccounts();
//		
//		if(accounts != null) {
//			for(DBInstanceAccount account : accounts) {
//				System.out.println(account.getAccountName());
//				
//			}
//		}
//	}
//
//	@Test
//	public void testDescribeDatabases() {
//		DescribeDatabasesRequest describeDatabasesRequest = new DescribeDatabasesRequest();
//		describeDatabasesRequest.setdBInstanceId(AliyunConstants.DB_INSTANCE_ID);
//		AliyunUtils.setClient(client);
//		DescribeDatabasesResponse response = AliyunUtils.execute(describeDatabasesRequest);
//
//		List<Database> bases = response.getDatabases();
//		
//		if(bases != null) {
//			for(Database db : bases) {
//				System.out.println(db.getdBName() + "--" + db.getEngine());
//			}
//		}
//		
//	}
}
