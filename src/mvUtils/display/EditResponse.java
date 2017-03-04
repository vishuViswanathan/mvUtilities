package mvUtils.display;

/**
 * Created by IntelliJ IDEA.
 * User: M Viswanathan
 * Date: 18-May-15
 * Time: 12:45 PM
 * To change this template use File | Settings | File Templates.
 */
public interface EditResponse {
    enum Response {
        SAVE("Save"),
        EXIT("Exit"),
        DELETE("Delete"),
        RESET("Reset");
        private final String responseStr;

        Response(String modeName) {
            this.responseStr = modeName;
        }

        public String getValue() {
            return name();
        }

        @Override
        public String toString() {
            return responseStr;
        }

        public static Response getEnum(String text) {
            Response retVal = null;
            if (text != null) {
              for (Response b : Response.values()) {
                if (text.equalsIgnoreCase(b.responseStr)) {
                  retVal = b;
                    break;
                }
              }
            }
            return retVal;
          }
    }
}
