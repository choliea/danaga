package com.danaga.exception.product;

public class AlreadyExistsException extends ProductCustomException {
	
	public AlreadyExistsException(ProductExceptionMsg msg) {
		super(msg);
	}
	public AlreadyExistsException(String data,ProductExceptionMsg msg) {
		super(data,msg);
	}

	public static class ExistsRecentViewException extends AlreadyExistsException{
		public ExistsRecentViewException(String data) {
			super(data,ProductExceptionMsg.ALREADY_EXISTS_RECENTVIEW);
		}
		public ExistsRecentViewException() {
			super(ProductExceptionMsg.ALREADY_EXISTS_RECENTVIEW);
		}
	}
	public static class ExistsInterestException extends AlreadyExistsException{
		public ExistsInterestException(String data) {
			super(data,ProductExceptionMsg.ALREADY_EXISTS_INTEREST);
		}
		public ExistsInterestException() {
			super(ProductExceptionMsg.ALREADY_EXISTS_INTEREST);
		}
	}
}
