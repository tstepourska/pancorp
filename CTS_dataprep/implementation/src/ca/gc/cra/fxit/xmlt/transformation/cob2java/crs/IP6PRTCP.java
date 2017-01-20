package ca.gc.cra.fxit.xmlt.transformation.cob2java.crs;
import ca.gc.ccra.rccr.cobol.*;
import java.util.*;

/** GENERATED BY COB2JAVA.
	MANUAL MAINTENANCE IS NOT RECOMMENDED

	<pre>
	Record Layout:

	  Offset  Length  Occ  Type Name
	  ======  ======  ===  ==== ==========================
		0		257		1	G	Prt18CntrlPersonInfo
		0		4		1	9		TransCd
		4		4		1	9		OaCpSqnbr
		8		2		5	G		CntrlPrsnRsdCntryCd
		n/a		2		1	X			OaCpRsdCntryCd
		18		9		1	X		OaCpFgnTin
		27		2		1	X		OaCpFgnTinCntryCd
		29		90		1	G		CntrlPersonNameGrp
		29		30		1	X			OaCpGvnNm
		59		30		1	X			OaCpMidNm
		89		30		1	X			OaCpSnm
		119		130		1	G		CntrlPersonAddressGrp
		119		30		1	X			OaCpAddrL1Txt
		149		30		1	X			OaCpAddrL2Txt
		179		28		1	X			OaCpCtyNm
		207		30		1	X			OaCpPvstNm
		237		2		1	X			OaCpCntryCd
		239		10		1	X			OaCpPstlZipCd
		249		8		1	G		CntrlPersonBirthGrp
		249		4		1	X			OaCpBrthYr
		253		2		1	X			OaCpBrthMo
		255		2		1	X			OaCpBrthDy
	</pre>
	@see rccr.cobol.CobolRecord
*/

@SuppressWarnings({"all"})
public class IP6PRTCP extends CobolRecord
{
	private static HashMap h = new HashMap();

	private static TreeSet fields = new TreeSet(new MVComparator());

	private static int fieldCount = 0;
	private static void put( CobolField f ) {
		h.put(f.getName(), new MapValue( fieldCount++, f ));
	}

	private static void init0() {
		put(new CobolField("Prt18CntrlPersonInfo",0,257,null,true));
		put(new CFNumeric("TransCd",0,4,null,0));
		put(new CFNumeric("OaCpSqnbr",4,4,null,0));
		put(new CobolField("CntrlPrsnRsdCntryCd_0",8,2,null,true));
		put(new CobolField("CntrlPrsnRsdCntryCd_1",10,2,null,true));
		put(new CobolField("CntrlPrsnRsdCntryCd_2",12,2,null,true));
		put(new CobolField("CntrlPrsnRsdCntryCd_3",14,2,null,true));
		put(new CobolField("CntrlPrsnRsdCntryCd_4",16,2,null,true));
		put(new CobolField("OaCpRsdCntryCd_0",8,2,null,false));
		put(new CobolField("OaCpRsdCntryCd_1",10,2,null,false));
		put(new CobolField("OaCpRsdCntryCd_2",12,2,null,false));
		put(new CobolField("OaCpRsdCntryCd_3",14,2,null,false));
		put(new CobolField("OaCpRsdCntryCd_4",16,2,null,false));
		put(new CobolField("OaCpFgnTin",18,9,null,false));
		put(new CobolField("OaCpFgnTinCntryCd",27,2,null,false));
		put(new CobolField("CntrlPersonNameGrp",29,90,null,true));
		put(new CobolField("OaCpGvnNm",29,30,null,false));
		put(new CobolField("OaCpMidNm",59,30,null,false));
		put(new CobolField("OaCpSnm",89,30,null,false));
		put(new CobolField("CntrlPersonAddressGrp",119,130,null,true));
		put(new CobolField("OaCpAddrL1Txt",119,30,null,false));
		put(new CobolField("OaCpAddrL2Txt",149,30,null,false));
		put(new CobolField("OaCpCtyNm",179,28,null,false));
		put(new CobolField("OaCpPvstNm",207,30,null,false));
		put(new CobolField("OaCpCntryCd",237,2,null,false));
		put(new CobolField("OaCpPstlZipCd",239,10,null,false));
		put(new CobolField("CntrlPersonBirthGrp",249,8,null,true));
		put(new CobolField("OaCpBrthYr",249,4,null,false));
		put(new CobolField("OaCpBrthMo",253,2,null,false));
		put(new CobolField("OaCpBrthDy",255,2,null,false));
	}

	static { 
		init0();
		fields.addAll(h.values());
	}

	public IP6PRTCP()
	{
		super(h, fields);
		rec = new byte[257];

		initialize();
	}

	public String getPrt18CntrlPersonInfo() { 
		return getString("Prt18CntrlPersonInfo");
	}

	public void setPrt18CntrlPersonInfo( String val ) { 
		set( "Prt18CntrlPersonInfo", val );
	}

	public int getTransCd() { 
		return getInt("TransCd");
	}

	public void setTransCd( int val ) { 
		set( "TransCd", val );
	}

	public int getOaCpSqnbr() { 
		return getInt("OaCpSqnbr");
	}

	public void setOaCpSqnbr( int val ) { 
		set( "OaCpSqnbr", val );
	}

	public String getCntrlPrsnRsdCntryCd(int i) { 
		return getString("CntrlPrsnRsdCntryCd" + "_" + i);
	}

	public void setCntrlPrsnRsdCntryCd( String val, int i ) { 
		set( "CntrlPrsnRsdCntryCd" + "_" + i, val );
	}

	public String getOaCpRsdCntryCd(int i) { 
		return getString("OaCpRsdCntryCd" + "_" + i);
	}

	public void setOaCpRsdCntryCd( String val, int i ) { 
		set( "OaCpRsdCntryCd" + "_" + i, val );
	}

	public String getOaCpFgnTin() { 
		return getString("OaCpFgnTin");
	}

	public void setOaCpFgnTin( String val ) { 
		set( "OaCpFgnTin", val );
	}

	public String getOaCpFgnTinCntryCd() { 
		return getString("OaCpFgnTinCntryCd");
	}

	public void setOaCpFgnTinCntryCd( String val ) { 
		set( "OaCpFgnTinCntryCd", val );
	}

	public String getCntrlPersonNameGrp() { 
		return getString("CntrlPersonNameGrp");
	}

	public void setCntrlPersonNameGrp( String val ) { 
		set( "CntrlPersonNameGrp", val );
	}

	public String getOaCpGvnNm() { 
		return getString("OaCpGvnNm");
	}

	public void setOaCpGvnNm( String val ) { 
		set( "OaCpGvnNm", val );
	}

	public String getOaCpMidNm() { 
		return getString("OaCpMidNm");
	}

	public void setOaCpMidNm( String val ) { 
		set( "OaCpMidNm", val );
	}

	public String getOaCpSnm() { 
		return getString("OaCpSnm");
	}

	public void setOaCpSnm( String val ) { 
		set( "OaCpSnm", val );
	}

	public String getCntrlPersonAddressGrp() { 
		return getString("CntrlPersonAddressGrp");
	}

	public void setCntrlPersonAddressGrp( String val ) { 
		set( "CntrlPersonAddressGrp", val );
	}

	public String getOaCpAddrL1Txt() { 
		return getString("OaCpAddrL1Txt");
	}

	public void setOaCpAddrL1Txt( String val ) { 
		set( "OaCpAddrL1Txt", val );
	}

	public String getOaCpAddrL2Txt() { 
		return getString("OaCpAddrL2Txt");
	}

	public void setOaCpAddrL2Txt( String val ) { 
		set( "OaCpAddrL2Txt", val );
	}

	public String getOaCpCtyNm() { 
		return getString("OaCpCtyNm");
	}

	public void setOaCpCtyNm( String val ) { 
		set( "OaCpCtyNm", val );
	}

	public String getOaCpPvstNm() { 
		return getString("OaCpPvstNm");
	}

	public void setOaCpPvstNm( String val ) { 
		set( "OaCpPvstNm", val );
	}

	public String getOaCpCntryCd() { 
		return getString("OaCpCntryCd");
	}

	public void setOaCpCntryCd( String val ) { 
		set( "OaCpCntryCd", val );
	}

	public String getOaCpPstlZipCd() { 
		return getString("OaCpPstlZipCd");
	}

	public void setOaCpPstlZipCd( String val ) { 
		set( "OaCpPstlZipCd", val );
	}

	public String getCntrlPersonBirthGrp() { 
		return getString("CntrlPersonBirthGrp");
	}

	public void setCntrlPersonBirthGrp( String val ) { 
		set( "CntrlPersonBirthGrp", val );
	}

	public String getOaCpBrthYr() { 
		return getString("OaCpBrthYr");
	}

	public void setOaCpBrthYr( String val ) { 
		set( "OaCpBrthYr", val );
	}

	public String getOaCpBrthMo() { 
		return getString("OaCpBrthMo");
	}

	public void setOaCpBrthMo( String val ) { 
		set( "OaCpBrthMo", val );
	}

	public String getOaCpBrthDy() { 
		return getString("OaCpBrthDy");
	}

	public void setOaCpBrthDy( String val ) { 
		set( "OaCpBrthDy", val );
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		
		sb
		.append("\n" + this.getClass().getSimpleName()+ ":getCntrlPersonAddressGrp=").append(this.getCntrlPersonAddressGrp()).append(",")
		.append("getTransCd=").append(this.getTransCd()).append(",")
		.append("getCntrlPersonBirthGrp=").append(this.getCntrlPersonBirthGrp()).append(",")
		.append("getCntrlPersonNameGrp=").append(this.getCntrlPersonNameGrp()).append(",")
		.append("getCntrlPrsnRsdCntryCd(0)=").append(this.getCntrlPrsnRsdCntryCd(0)).append(",")
		.append("getRec=").append(this.getRec().trim()).append(",")
		.append("getOaCpAddrL1Txt=").append(this.getOaCpAddrL1Txt()).append(",")
		.append("getOaCpAddrL2Txt=").append(this.getOaCpAddrL2Txt()).append(",")
		.append("getOaCpBrthDy=").append(this.getOaCpBrthDy()).append(",")
		.append("getOaCpBrthMo=").append(this.getOaCpBrthMo()).append(",")
		.append("getOaCpBrthYr=").append(this.getOaCpBrthYr()).append(",")
		.append("getOaCpCntryCd=").append(this.getOaCpCntryCd()).append(",")
		.append("getOaCpCtyNm=").append(this.getOaCpCtyNm()).append(",")
		.append("getOaCpFgnTin=").append(this.getOaCpFgnTin()).append(",")
		.append("getOaCpFgnTinCntryCd=").append(this.getOaCpFgnTinCntryCd()).append(",")
		.append("getOaCpGvnNm=").append(this.getOaCpGvnNm()).append(",")
		.append("getOaCpMidNm=").append(this.getOaCpMidNm()).append(",")
		.append("getOaCpPstlZipCd=").append(this.getOaCpPstlZipCd()).append(",")
		.append("getOaCpPvstNm=").append(this.getOaCpPvstNm()).append(",")
		.append("getOaCpRsdCntryCd(0)=").append(this.getOaCpRsdCntryCd(0)).append(",")
		.append("getOaCpSnm=").append(this.getOaCpSnm()).append(",")
		.append("getOaCpSqnbr=").append(this.getOaCpSqnbr()).append(",")
		.append("getPrt18CntrlPersonInfo=").append(this.getPrt18CntrlPersonInfo()).append(",")
		
		;
		
		return sb.toString();
	}

}