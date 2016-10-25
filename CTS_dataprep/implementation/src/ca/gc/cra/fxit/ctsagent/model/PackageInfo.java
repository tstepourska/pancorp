package ca.gc.cra.fxit.ctsagent.model;

public class PackageInfo {
	
	/**
	 * Specfies direction of the transaction:
	 * inbound (I) or outbound (O)
	 */
	private String jobDirection;
	
	/**
	 * Additional job detail
	 */
	private String jobSuffix;
	
	private int splitFileCount = 0;
	
	/**
	 * Specifies type of the package processed:
	 * data (D) or status message (S)
	 */
	private String packageType;

	private int taxYear;
	
	private String sendingCountry;
	private String receivingCountry;

	private String inputPathName;
	private String messageRefId;
	
	private String origFilename;
	
	private String headerLine;
	
	private String dataOwnerPrefix;
	
	
	
	/**
	 * @return the dataOwnerPrefix
	 */
	public String getDataOwnerPrefix() {
		return dataOwnerPrefix;
	}

	/**
	 * @param dataOwnerPrefix the dataOwnerPrefix to set
	 */
	public void setDataOwnerPrefix(String dataOwnerPrefix) {
		this.dataOwnerPrefix = dataOwnerPrefix;
	}

	/**
	 * @return the headerLine
	 */
	public String getHeaderLine() {
		return headerLine;
	}

	/**
	 * @param headerLine the headerLine to set
	 */
	public void setHeaderLine(String headerLine) {
		this.headerLine = headerLine;
	}

	/**
	 * @return the splitFileCount
	 */
	public int getSplitFileCount() {
		return splitFileCount;
	}

	/**
	 * @param splitFileCount the splitFileCount to set
	 */
	public void setSplitFileCount(int splitFileCount) {
		this.splitFileCount = splitFileCount;
	}


	/**
	 * @return the jobSuffix
	 */
	public String getJobSuffix() {
		return jobSuffix;
	}

	/**
	 * @param jobSuffix the jobSuffix to set
	 */
	public void setJobSuffix(String jobSuffix) {
		this.jobSuffix = jobSuffix;
	}


	/**
	 * @return the origFilename
	 */
	public String getOrigFilename() {
		return origFilename;
	}

	/**
	 * @param origFilename the origFilename to set
	 */
	public void setOrigFilename(String origFilename) {
		this.origFilename = origFilename;
	}

	/**
	 * @return the receivingCountry
	 */
	public String getReceivingCountry() {
		return receivingCountry;
	}

	/**
	 * @param receivingCountry the receivingCountry to set
	 */
	public void setReceivingCountry(String receivingCountry) {
		this.receivingCountry = receivingCountry;
	}

	/**
	 * @return the sendingCountry
	 */
	public String getSendingCountry() {
		return sendingCountry;
	}

	/**
	 * @param sendingCountry the sendingCountry to set
	 */
	public void setSendingCountry(String sendingCountry) {
		this.sendingCountry = sendingCountry;
	}
	
	
	/**
	 * @return the jobDirection
	 */
	public String getJobDirection() {
		return jobDirection;
	}

	/**
	 * @param jobDirection the jobDirection to set
	 */
	public void setJobDirection(String jobDirection) {
		this.jobDirection = jobDirection;
	}

	/**
	 * @return the taxYear
	 */
	public int getTaxYear() {
		return taxYear;
	}

	/**
	 * @param taxYear the taxYear to set
	 */
	public void setTaxYear(int taxYear) {
		this.taxYear = taxYear;
	}
	
	/**
	 * Location of the XSD schema file
	 */
	private String schemaPath;
	
	/**
	 * Location of the XML file
	 */
	private String xmlFilename;
	
	/**
	 * @return the xmlFilename
	 */
	public String getXmlFilename() {
		return xmlFilename;
	}

	/**
	 * @param xmlFilename the xmlFilename to set
	 */
	public void setXmlFilename(String xmlPath) {
		this.xmlFilename = xmlPath;
	}
	
	/**
	 * @return the schemaPath
	 */
	public String getSchemaPath() {
		return schemaPath;
	}

	/**
	 * @param schemaPath the schemaPath to set
	 */
	public void setSchemaPath(String schemaPath) {
		this.schemaPath = schemaPath;
	}

	public PackageInfo(){
		
	}	
	
	/**
	 * @return the pkType
	 */
	public String getPackageType() {
		return packageType;
	}

	/**
	 * @param pkType the pkType to set
	 */
	public void setPackageType(String pType) {
		this.packageType = pType;
	}

	public String getMessageRefId(){
		return this.messageRefId;
	}
	
	public void setMessageRefId(String s){
		this.messageRefId = s;
	}
	
	public String getInputPathName(){
		return this.inputPathName;
	}
	
	public void setInputPathName(String s){
		this.inputPathName = s;
	}
}
