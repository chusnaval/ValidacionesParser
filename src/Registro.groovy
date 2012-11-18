
/**
 * Created with IntelliJ IDEA.
 * User: chus
 * Date: 18/11/12
 * Time: 14:13
 */
class Registro {

    private String numeroExpediente
    private String nombreTramo
    private String originadora
    private List<String> errores = new ArrayList<String>()

    @Override
    public String toString() {
        return numeroExpediente +
                ";" + nombreTramo +
                ";" + originadora +
                ";" + toStringErrores();
    }

    private String toStringErrores(){
        StringBuffer sb = new StringBuffer()
        for (String error: errores){
            sb.append(error+";")
        }
        return sb.toString()
    }

    public boolean hasError(type){
        for (String error: errores){
            if(error.contains(type)){
                return true
            }
        }
        return false
    }

    public String toStringSinErrores(){
        return ";" + numeroExpediente +
                ";" + nombreTramo +
                ";" + originadora
    }
}
