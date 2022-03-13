/**
 * 
 */
package enums;

/**
 * @author rafael
 *
 */
public enum SOEnum {
	
	//https://nomics.com/docs/#section/SDKs-and-Libraries/Community-Submissions-(Not-supported-by-Nomics)
	WINDOWS ("C:/Temp/"),
    LINUX ("/tmp/"),
    MAC ("/tmp/");
	
	SOEnum (String so) {
        this.so = so;
    }
    
    public String so;
        
    public String getSOPath() {
        return so;
    }
//    
//    @Override
//    public String valueOf(String) {
//    	
//    }

}
