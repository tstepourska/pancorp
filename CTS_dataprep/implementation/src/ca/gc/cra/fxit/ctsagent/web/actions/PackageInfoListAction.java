package ca.gc.cra.fxit.ctsagent.web.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

import org.apache.struts2.interceptor.SessionAware;

import ca.gc.cra.fxit.ctsagent.model.PackageInfo;;

/**
 * Retrieve packageInfo by id.
 */
public class PackageInfoListAction extends ActionSupport implements Preparable, SessionAware {

    private static final long serialVersionUID = 1L;
    private Log log = LogFactory.getLog(getClass());
   // private SampleInterface handler = null;
    private Map<String,Object> session = null;
    
    private Integer requestedPage = null;

    private Integer rowsPerPage = 15;
    private Integer offset = null;
    private Integer currentPage = null;
    private Object[] pages = null;    
    private List<PackageInfo> packageInfoList = null;
    
    @SuppressWarnings("unchecked")
	@Override
	public void prepare() throws Exception {
        log.debug("prepare()");
        Context context = new InitialContext();
      //  handler = (SampleInterface)context.lookup("ca.gc.cra.tstest.business.SampleInterface");
        context.close();
                
        offset = (Integer)session.get("offset");
        if( offset == null ) {
            offset = 0;
        }
        
        packageInfoList = (List<PackageInfo>)session.get("packageInfoList");
        if( packageInfoList == null ) {
        	packageInfoList = new ArrayList<PackageInfo>();
        }

    }
    
    public String retrieveAll() throws Exception {
        log.debug("retrieveAll()");

       // employeeList = handler.getEmployees();
      //  log.debug("retrieved " + employeeList.size() + " employees");
      //  session.put("employeeList", employeeList);
        
      //  offset = 0;            
        
       // saveLocation(offset);
        
        return SUCCESS;
    }

    public String previous() throws Exception {
        if( offset > 0 ) {
            offset = Math.max(offset - rowsPerPage, 0);
        }
        saveLocation(offset);
        
        return SUCCESS;
    }
    
    
    public String next() throws Exception {
        if(packageInfoList.size() >= offset + rowsPerPage ) {
            offset += rowsPerPage;
        }
        saveLocation(offset);
        
        return SUCCESS;
    }
    
    public String retrievePage() throws Exception {
        log.debug("retrievePage()");

        // if requestedPage is null (which will be the case
        // when we're invoked due to a language switch)
        // we'll just redisplay the current page.
        if( requestedPage != null ) {
            offset = requestedPage.intValue() * rowsPerPage;
        }
        
        saveLocation(offset);
        
        return SUCCESS;
    }
    
    @Override
	public void setSession(Map<String,Object> session) {
        this.session = session;
    }
    
    private void saveLocation(int offset) {
        session.put("offset", offset);
        currentPage = getCurrentPage();
    }

	
    public void setRequestedPage(Integer requestedPage) {
        this.requestedPage = requestedPage;
    }

    public Integer getRowsPerPage() {
        return rowsPerPage;
    }

    public Integer getOffset() {
        if( offset == null ) {
            offset = 0;
        }
        return offset;
    }

    public List<PackageInfo> getPackageInfoList() {
        return packageInfoList;
    }

    public Integer getCurrentPage() {
        if( currentPage == null ) {
            currentPage = new Integer(getOffset() / getRowsPerPage());
        }
        return currentPage;
    }

    /**
     * Create an arbitrary list representing the available pages,
     * for the benefit of the jsp's iterator tag.
     */
    public Object[] getPages() {
        if( pages == null ) {
            int pageCount = packageInfoList.size() / rowsPerPage;
            if( packageInfoList.size() % rowsPerPage != 0 ) {
                pageCount++;
            }
            pages = new Object[pageCount];
        }
        return pages;
    }
    
    public boolean showPrevious() {
        return( offset > 0 );
    }

    public boolean showNext() {
        return(packageInfoList.size() > offset + rowsPerPage );
    }
    
    public boolean showLink(Integer pageIndex) {
        if( (pageIndex < 3 && currentPage < 3 ) ||
            (pageIndex > currentPage - 2 && pageIndex < currentPage + 2 ) 
          ) {
            return true;
        }

        return false;
    }
    
    public boolean showEllipsis(Integer pageIndex) {
        if( pageIndex == 0 && currentPage > 1 ) {
            return true;
        }
        if( pageIndex == pages.length - 1 && currentPage != pages.length - 1 ) {
            return true;
        }
        
        return false;
    }
	
}
