package ca.gc.cra.fxit.xmlt.generated.cob2java.crs;
import ca.gc.ccra.rccr.cobol.*;
import java.util.*;

/** GENERATED BY COB2JAVA.
	MANUAL MAINTENANCE IS NOT RECOMMENDED

	<pre>
	Record Layout:

	  Offset  Length  Occ  Type Name
	  ======  ======  ===  ==== ==========================
		0		214		1	G	Prt18AhldrInfo
		0		4		1	9		TransCd
		4		130		1	G		AccountHldrAddressGrp
		4		30		1	X			FiAhAddrL1Txt
		34		30		1	X			FiAhAddrL2Txt
		64		28		1	X			FiAhCtyNm
		92		30		1	X			FiAhPvstNm
		122		2		1	X			FiAhCntryCd
		124		10		1	X			FiAhPstlZipCd
		134		17		1	X		FiAbamt
		151		15		1	X		FiAhDvamt
		166		15		1	X		FiAhIntamt
		181		15		1	X		FiAhGpramt
		196		15		1	X		FiAhOamt
		211		3		1	X		FiAcctCrcyTcd
	</pre>
	@see rccr.cobol.CobolRecord
*/

@SuppressWarnings({"all"})
public class IP6PRTAC extends CobolRecord
{
	private static HashMap h = new HashMap();

	private static TreeSet fields = new TreeSet(new MVComparator());

	private static int fieldCount = 0;
	private static void put( CobolField f ) {
		h.put(f.getName(), new MapValue( fieldCount++, f ));
	}

	private static void init0() {
		put(new CobolField("Prt18AhldrInfo",0,214,null,true));
		put(new CFNumeric("TransCd",0,4,null,0));
		put(new CobolField("AccountHldrAddressGrp",4,130,null,true));
		put(new CobolField("FiAhAddrL1Txt",4,30,null,false));
		put(new CobolField("FiAhAddrL2Txt",34,30,null,false));
		put(new CobolField("FiAhCtyNm",64,28,null,false));
		put(new CobolField("FiAhPvstNm",92,30,null,false));
		put(new CobolField("FiAhCntryCd",122,2,null,false));
		put(new CobolField("FiAhPstlZipCd",124,10,null,false));
		put(new CobolField("FiAbamt",134,17,null,false));
		put(new CobolField("FiAhDvamt",151,15,null,false));
		put(new CobolField("FiAhIntamt",166,15,null,false));
		put(new CobolField("FiAhGpramt",181,15,null,false));
		put(new CobolField("FiAhOamt",196,15,null,false));
		put(new CobolField("FiAcctCrcyTcd",211,3,null,false));
	}

	static { 
		init0();
		fields.addAll(h.values());
	}

	public IP6PRTAC()
	{
		super(h, fields);
		rec = new byte[214];

		initialize();
	}

	public String getPrt18AhldrInfo() { 
		return getString("Prt18AhldrInfo");
	}

	public void setPrt18AhldrInfo( String val ) { 
		set( "Prt18AhldrInfo", val.trim() );
	}

	public int getTransCd() { 
		return getInt("TransCd");
	}

	public void setTransCd( int val ) { 
		set( "TransCd", val );
	}

	public String getAccountHldrAddressGrp() { 
		return getString("AccountHldrAddressGrp");
	}

	public void setAccountHldrAddressGrp( String val ) { 
		set( "AccountHldrAddressGrp", val.trim() );
	}

	public String getFiAhAddrL1Txt() { 
		return getString("FiAhAddrL1Txt");
	}

	public void setFiAhAddrL1Txt( String val ) { 
		set( "FiAhAddrL1Txt", val.trim() );
	}

	public String getFiAhAddrL2Txt() { 
		return getString("FiAhAddrL2Txt");
	}

	public void setFiAhAddrL2Txt( String val ) { 
		set( "FiAhAddrL2Txt", val.trim() );
	}

	public String getFiAhCtyNm() { 
		return getString("FiAhCtyNm");
	}

	public void setFiAhCtyNm( String val ) { 
		set( "FiAhCtyNm", val.trim() );
	}

	public String getFiAhPvstNm() { 
		return getString("FiAhPvstNm");
	}

	public void setFiAhPvstNm( String val ) { 
		set( "FiAhPvstNm", val.trim() );
	}

	public String getFiAhCntryCd() { 
		return getString("FiAhCntryCd");
	}

	public void setFiAhCntryCd( String val ) { 
		set( "FiAhCntryCd", val.trim() );
	}

	public String getFiAhPstlZipCd() { 
		return getString("FiAhPstlZipCd");
	}

	public void setFiAhPstlZipCd( String val ) { 
		set( "FiAhPstlZipCd", val.trim() );
	}

	public String getFiAbamt() { 
		return getString("FiAbamt");
	}

	public void setFiAbamt( String val) { 
		set( "FiAbamt", val.trim() );
	}

	public String getFiAhDvamt() { 
		return getString("FiAhDvamt");
	}

	public void setFiAhDvamt( String val ) { 
		set( "FiAhDvamt", val.trim() );
	}

	public String getFiAhIntamt() { 
		return getString("FiAhIntamt");
	}

	public void setFiAhIntamt( String val ) { 
		set( "FiAhIntamt", val.trim() );
	}

	public String getFiAhGpramt() { 
		return getString("FiAhGpramt");
	}

	public void setFiAhGpramt( String val ) { 
		set( "FiAhGpramt", val.trim());
	}

	public String getFiAhOamt() { 
		return getString("FiAhOamt");
	}

	public void setFiAhOamt( String val ) { 
		set( "FiAhOamt", val.trim() );
	}

	public String getFiAcctCrcyTcd() { 
		return getString("FiAcctCrcyTcd");
	}

	public void setFiAcctCrcyTcd( String val ) { 
		set( "FiAcctCrcyTcd", val.trim() );
	}

	public String toString(){
		StringBuilder sb = new StringBuilder();
		
		sb
		.append("\n" + this.getClass().getSimpleName()+ ":getAccountHldrAddressGrp=").append(this.getAccountHldrAddressGrp()).append(",")
		.append("getTransCd=").append(this.getTransCd()).append(",")
		.append("getFiAbamt=").append(this.getFiAbamt()).append(",")
		.append("getFiAcctCrcyTcd=").append(this.getFiAcctCrcyTcd()).append(",")
		.append("getFiAhAddrL1Txt=").append(this.getFiAhAddrL1Txt()).append(",")
		.append("getRec=").append(this.getRec().trim()).append(",")
		.append("getFiAhAddrL2Txt=").append(this.getFiAhAddrL2Txt()).append(",")
		.append("getFiAhCntryCd=").append(this.getFiAhCntryCd()).append(",")
		.append("getFiAhCtyNm=").append(this.getFiAhCtyNm()).append(",")
		.append("getFiAhDvamt=").append(this.getFiAhDvamt()).append(",")
		.append("getFiAhGpramt=").append(this.getFiAhGpramt()).append(",")
		.append("getFiAhIntamt=").append(this.getFiAhIntamt()).append(",")
		.append("getFiAhOamt=").append(this.getFiAhOamt()).append(",")
		.append("getFiAhPstlZipCd=").append(this.getFiAhPstlZipCd()).append(",")
		.append("getFiAhPvstNm=").append(this.getFiAhPvstNm()).append(",")
		.append("getPrt18AhldrInfo=").append(this.getPrt18AhldrInfo()).append(",")
	
		;
		
		return sb.toString();
	}
}