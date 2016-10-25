package ca.gc.cra.fxit.ctsagent.generated.cob2java.ftc;
import ca.gc.ccra.rccr.cobol.*;
import java.util.*;

/** GENERATED BY COB2JAVA.
	MANUAL MAINTENANCE IS NOT RECOMMENDED

	<pre>
	Record Layout:

	  Offset  Length  Occ  Type Name
	  ======  ======  ===  ==== ==========================
		0		28		1	G	Prt18HdrRec
		0		4		1	9		TransCd
		4		8		1	9		Prt18RecCount
		12		8		1	9		RunDate
		20		8		1	9		RunTime
	</pre>
	@see rccr.cobol.CobolRecord
*/

@SuppressWarnings({"all"})
public class IP6MSGTR extends CobolRecord
{
	private static HashMap h = new HashMap();

	private static TreeSet fields = new TreeSet(new MVComparator());

	private static int fieldCount = 0;
	private static void put( CobolField f ) {
		h.put(f.getName(), new MapValue( fieldCount++, f ));
	}

	private static void init0() {
		put(new CobolField("Prt18HdrRec",0,28,null,true));
		put(new CFNumeric("TransCd",0,4,null,0));
		put(new CFNumeric("Prt18RecCount",4,8,null,0));
		put(new CFNumeric("RunDate",12,8,null,0));
		put(new CFNumeric("RunTime",20,8,null,0));
	}

	static { 
		init0();
		fields.addAll(h.values());
	}

	public IP6MSGTR()
	{
		super(h, fields);
		rec = new byte[28];

		initialize();
	}

	public String getPrt18HdrRec() { 
		return getString("Prt18HdrRec");
	}

	public void setPrt18HdrRec( String val ) { 
		set( "Prt18HdrRec", val.trim() );
	}

	public int getTransCd() { 
		return getInt("TransCd");
	}

	public void setTransCd( int val ) { 
		set( "TransCd", val );
	}

	public int getPrt18RecCount() { 
		return getInt("Prt18RecCount");
	}

	public void setPrt18RecCount( int val ) { 
		set( "Prt18RecCount", val );
	}

	public int getRunDate() { 
		return getInt("RunDate");
	}

	public void setRunDate( int val ) { 
		set( "RunDate", val );
	}

	public int getRunTime() { 
		return getInt("RunTime");
	}

	public void setRunTime( int val ) { 
		set( "RunTime", val );
	}

	public String toString(){
		StringBuilder sb = new StringBuilder();
		
		sb
		.append("\n" + this.getClass().getSimpleName()+ ":getPrt18HdrRec=").append(this.getPrt18HdrRec()).append(",")
		.append("getTransCd=").append(this.getTransCd()).append(",")
		.append("getPrt18RecCount=").append(this.getPrt18RecCount()).append(",")
		.append("getRunDate=").append(this.getRunDate()).append(",")
		.append("getPrt18CntrlPersonInfo=").append(this.getRunDate()).append(",")
		.append("getRec=").append(this.getRec().trim()).append(",")
		;
		
		return sb.toString();
	}
}