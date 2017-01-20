package ca.gc.cra.fxit.xmlt.transformation.cob2java.crs;
import ca.gc.ccra.rccr.cobol.*;
import java.util.*;

/** GENERATED BY COB2JAVA.
	MANUAL MAINTENANCE IS NOT RECOMMENDED

	<pre>
	Record Layout:

	  Offset  Length  Occ  Type Name
	  ======  ======  ===  ==== ==========================
		0		307		1	G	Prt18SponsorInfo
		0		4		1	9		TransCd
		4		1		1	X		FispInfoDtyCd
		5		40		1	X		FispDocRefId
		45		40		1	X		FispCorrDocRefId
		85		2		1	X		FispRsdCntryCd
		87		20		1	X		FispGiin
		107		70		1	G		SponsorNmGrp
		107		35		1	X			FispNmL1Txt
		142		35		1	X			FispNmL2Txt
		177		130		1	G		SponsorAddressGrp
		177		30		1	X			FispAddrL1Txt
		207		30		1	X			FispAddrL2Txt
		237		28		1	X			FispCtyNm
		265		30		1	X			FispPvstNm
		295		2		1	X			FispCntryCd
		297		10		1	X			FispPstlZipCd
	</pre>
	@see rccr.cobol.CobolRecord
*/

@SuppressWarnings({"all"})
public class IP6PRTSP extends CobolRecord
{
	private static HashMap h = new HashMap();

	private static TreeSet fields = new TreeSet(new MVComparator());

	private static int fieldCount = 0;
	private static void put( CobolField f ) {
		h.put(f.getName(), new MapValue( fieldCount++, f ));
	}

	private static void init0() {
		put(new CobolField("Prt18SponsorInfo",0,307,null,true));
		put(new CFNumeric("TransCd",0,4,null,0));
		put(new CobolField("FispInfoDtyCd",4,1,null,false));
		put(new CobolField("FispDocRefId",5,40,null,false));
		put(new CobolField("FispCorrDocRefId",45,40,null,false));
		put(new CobolField("FispRsdCntryCd",85,2,null,false));
		put(new CobolField("FispGiin",87,20,null,false));
		put(new CobolField("SponsorNmGrp",107,70,null,true));
		put(new CobolField("FispNmL1Txt",107,35,null,false));
		put(new CobolField("FispNmL2Txt",142,35,null,false));
		put(new CobolField("SponsorAddressGrp",177,130,null,true));
		put(new CobolField("FispAddrL1Txt",177,30,null,false));
		put(new CobolField("FispAddrL2Txt",207,30,null,false));
		put(new CobolField("FispCtyNm",237,28,null,false));
		put(new CobolField("FispPvstNm",265,30,null,false));
		put(new CobolField("FispCntryCd",295,2,null,false));
		put(new CobolField("FispPstlZipCd",297,10,null,false));
	}

	static { 
		init0();
		fields.addAll(h.values());
	}

	public IP6PRTSP()
	{
		super(h, fields);
		rec = new byte[307];

		initialize();
	}

	public String getPrt18SponsorInfo() { 
		return getString("Prt18SponsorInfo");
	}

	public void setPrt18SponsorInfo( String val ) { 
		set( "Prt18SponsorInfo", val );
	}

	public int getTransCd() { 
		return getInt("TransCd");
	}

	public void setTransCd( int val ) { 
		set( "TransCd", val );
	}

	public String getFispInfoDtyCd() { 
		return getString("FispInfoDtyCd");
	}

	public void setFispInfoDtyCd( String val ) { 
		set( "FispInfoDtyCd", val );
	}

	public String getFispDocRefId() { 
		return getString("FispDocRefId");
	}

	public void setFispDocRefId( String val ) { 
		set( "FispDocRefId", val );
	}

	public String getFispCorrDocRefId() { 
		return getString("FispCorrDocRefId");
	}

	public void setFispCorrDocRefId( String val ) { 
		set( "FispCorrDocRefId", val );
	}

	public String getFispRsdCntryCd() { 
		return getString("FispRsdCntryCd");
	}

	public void setFispRsdCntryCd( String val ) { 
		set( "FispRsdCntryCd", val );
	}

	public String getFispGiin() { 
		return getString("FispGiin");
	}

	public void setFispGiin( String val ) { 
		set( "FispGiin", val );
	}

	public String getSponsorNmGrp() { 
		return getString("SponsorNmGrp");
	}

	public void setSponsorNmGrp( String val ) { 
		set( "SponsorNmGrp", val );
	}

	public String getFispNmL1Txt() { 
		return getString("FispNmL1Txt");
	}

	public void setFispNmL1Txt( String val ) { 
		set( "FispNmL1Txt", val );
	}

	public String getFispNmL2Txt() { 
		return getString("FispNmL2Txt");
	}

	public void setFispNmL2Txt( String val ) { 
		set( "FispNmL2Txt", val );
	}

	public String getSponsorAddressGrp() { 
		return getString("SponsorAddressGrp");
	}

	public void setSponsorAddressGrp( String val ) { 
		set( "SponsorAddressGrp", val );
	}

	public String getFispAddrL1Txt() { 
		return getString("FispAddrL1Txt");
	}

	public void setFispAddrL1Txt( String val ) { 
		set( "FispAddrL1Txt", val );
	}

	public String getFispAddrL2Txt() { 
		return getString("FispAddrL2Txt");
	}

	public void setFispAddrL2Txt( String val ) { 
		set( "FispAddrL2Txt", val );
	}

	public String getFispCtyNm() { 
		return getString("FispCtyNm");
	}

	public void setFispCtyNm( String val ) { 
		set( "FispCtyNm", val );
	}

	public String getFispPvstNm() { 
		return getString("FispPvstNm");
	}

	public void setFispPvstNm( String val ) { 
		set( "FispPvstNm", val );
	}

	public String getFispCntryCd() { 
		return getString("FispCntryCd");
	}

	public void setFispCntryCd( String val ) { 
		set( "FispCntryCd", val );
	}

	public String getFispPstlZipCd() { 
		return getString("FispPstlZipCd");
	}

	public void setFispPstlZipCd( String val ) { 
		set( "FispPstlZipCd", val );
	}

}