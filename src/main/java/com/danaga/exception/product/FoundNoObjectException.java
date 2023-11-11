package com.danaga.exception.product;

public class FoundNoObjectException extends ProductCustomException {

	public FoundNoObjectException(ProductExceptionMsg msg) {
		super(msg);
	}
	public FoundNoObjectException(String data, ProductExceptionMsg msg) {
		super(data,msg);
	}
	public static class FoundNoMemberException extends FoundNoObjectException{
		public FoundNoMemberException(String data) {
			super(data,ProductExceptionMsg.FOUND_NO_MEMBER);
		}
		public FoundNoMemberException() {
			super(ProductExceptionMsg.FOUND_NO_MEMBER);
		}
	}
	public static class FoundNoOptionSetException extends FoundNoObjectException{
		public FoundNoOptionSetException(String data, ProductExceptionMsg msg) {
			super(data, ProductExceptionMsg.FOUND_NO_OPTIONSET);
		}
		public FoundNoOptionSetException() {
			super(ProductExceptionMsg.FOUND_NO_OPTIONSET);
		}
	}
	public static class FoundNoRecentViewException extends FoundNoObjectException{
		public FoundNoRecentViewException(String data, ProductExceptionMsg msg) {
			super(data, ProductExceptionMsg.FOUND_NO_RECENTVIEW);
		}
		public FoundNoRecentViewException() {
			super(ProductExceptionMsg.FOUND_NO_RECENTVIEW);
		}
	}
	public static class FoundNoProductException extends FoundNoObjectException{
		public FoundNoProductException(String data, ProductExceptionMsg msg) {
			super(data, ProductExceptionMsg.FOUND_NO_PRODUCT);
		}
		public FoundNoProductException() {
			super(ProductExceptionMsg.FOUND_NO_PRODUCT);
		}
	}
	public static class FoundNoCategoryException extends FoundNoObjectException{
		public FoundNoCategoryException(String data, ProductExceptionMsg msg) {
			super(data, ProductExceptionMsg.FOUND_NO_CATEGORY);
		}
		public FoundNoCategoryException() {
			super(ProductExceptionMsg.FOUND_NO_CATEGORY);
		}
	}
	public static class FoundNoInterestException extends FoundNoObjectException {
		public FoundNoInterestException(String data, ProductExceptionMsg msg) {
			super(data, ProductExceptionMsg.FOUND_NO_INTEREST);
		}
		public FoundNoInterestException() {
			super(ProductExceptionMsg.FOUND_NO_INTEREST);
		}
	}
	public static class FoundNoOptionsException extends FoundNoObjectException {
		public FoundNoOptionsException(String data, ProductExceptionMsg msg) {
			super(data, ProductExceptionMsg.FOUND_NO_OPTIONS);
		}
		public FoundNoOptionsException() {
			super(ProductExceptionMsg.FOUND_NO_OPTIONS);
		}
	}
}