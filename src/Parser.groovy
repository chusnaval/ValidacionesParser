/**
 * Created with IntelliJ IDEA.
 * User: chus
 * Date: 18/11/12
 * Time: 14:15
 */
class Parser {
    def map = [:]
    def types = ["Validacion gestora pagos cobros: ",
            "Validacion no existe oficina gestora pagos cobros: ",
            "Debe dar de alta al menos una oficina para la entidad",
            "es vigente a partir de la fecha ",
            "no esta vigente a partir de la fecha",
            "solapan las fechas de Inicio",
            "falta uno o mas tramos durante la vida de la ",
            "Existen participaciones de Oficinas en periodos donde no participa su Entidad",
            "n no es del 100 por 100 a",
            "n de la oficina debe ser mayor o igual que la fecha inicio",
            "n de la oficina debe ser menor o igual que la fecha fin",
            "n oficina para el rol ",
            "n de las oficinas no es del 100 por 100 a",
            ", falta la entidad ",
            ", falta la oficina ",
            ", el porcentaje de entidades es incorrecto a",
            ", el porcentaje de oficinas es incorrecto a",
            ", la fecha fin de las oficinas es"]
    def translations = ["Periodos de participación en un tramo donde en un contrato sindicado no existe oficina gestora de pagos/cobros:",
            "Periodos de participación en un tramo donde en un contrato sindicado existe más de una oficina gestora de pagos/cobros:",
            "Periodos donde no existe participación de ninguna oficina en una fecha del expediente:",
            "Existen periodos donde una entidad participa antes de su vigencia:",
            "Existen periodos donde una entidad participa más allá de su vigencia:",
            "Periodos de participación donde una entidad participa dos veces al mismo tiempo:",
            "Periodos donde no existe participación de ninguna entidad en una fecha del expediente: ",
            "Existen participaciones de Oficinas en periodos donde no participa su Entidad",
            "Existen participaciones de Entidad distinta a 100%. La suma de la participación de las entidades participantes no es 100% a una fecha dada:",
            "Oficinas que participan en fechas antes de la participación de su entidad:",
            "Oficinas que participan en fechas después de la participación de su entidad:",
            "Existen periodos donde una entidad no posee una oficina a una fecha dada: ",
            "Participación de Oficinas distinta a 100%. La suma de la participación de las oficinas participantes no es 100% a una fecha dada:",
            "Repartos de patrones donde una entidad no tiene asignado porcentaje alguno:",
            "Repartos de patrones donde una oficina no tiene asignado porcentaje alguno: ",
            "Reparto de entidades distinta a 100%. La suma de la participación de las entidades participantes en un reparto no es 100% a una fecha dada:",
            "Reparto de oficinas distinta a 100%. La suma de la participación de las oficinas participantes en un reparto no es 100% a una fecha dada:",
            "Repartos de oficinas que existen más allá de la fecha de participación de la oficina:"]

    public static void main(args) {
        def f = new File('/Users/chus/dev/neoris/errores14.csv')
        def Parser parser = new Parser();
        f.eachLine ("CP1252"){
            it -> parser.procesarLinea(it)
        }

        Set entries = parser.map.keySet()
        for (String key : entries) {
            def salida = new FileWriter("/Users/chus/dev/neoris/resultado/" + key + ".xls", true)
            def BufferedWriter salidaBuffer = new BufferedWriter(salida)
            salida.write(parser.procesarOriginadora(parser.map.get(key)))
            salidaBuffer.flush();
            salidaBuffer.close();
            salida.close();
        }

    }

    def procesarOriginadora(List<Registro> registroList) {
        StringBuffer sb = new StringBuffer()
        int iterator = 0
        for (String type : types) {
            sb.append(translations[iterator]+"\n")
            for (Registro reg : registroList) {
                if (reg.hasError(type)) {
                    sb.append(reg.toStringSinErrores()+"\n")
                }
            }
            iterator++
        }

        for(Registro reg: registroList){
            for(String error : reg.errores){
                boolean encontrado = false
                for(String type : types){
                    if(error.contains(type)){
                        encontrado = true
                    }
                }
                if(!encontrado){
                    System.out.println("Tipo no encontrado: "+error+"\n")
                }
            }
        }

        return sb.toString()

    }

    def procesarLinea(linea) {
        def tokens = linea.tokenize(";")
        Registro reg = new Registro()
        reg.numeroExpediente = tokens[0]
        reg.nombreTramo = tokens[1]
        reg.originadora = tokens[2]

        for (int i = 3; i < tokens.size(); i++) {
            reg.errores.add(tokens[i])
        }

        if (map.containsKey(tokens[2])) {
            def lista = map.get(tokens[2])
            lista.add(reg)
        } else {
            def lista = []
            lista.add(reg)
            map.put(tokens[2], lista)
        }

    }


}
