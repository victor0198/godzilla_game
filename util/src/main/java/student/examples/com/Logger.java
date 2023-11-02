package student.examples.com;

public class Logger {
    private static Logger INSTANCE;
    private int destination = 1;

    private Logger() {
    }

    public static Logger getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Logger();
        }

        return INSTANCE;
    }

    public void setDestination(int destination){
        this.destination = destination;
    }

    private void write(String str){
        switch (destination){
            case 1:
                System.out.println(str);
                break;
            case 2:
                System.out.println("To file:"+str);
                break;
        }
    }

    public void info(String str){
        write("INFO: " + str);
    }

    public void warning(String str){
        write("WARNING: " + str);
    }

    public void error(String str){
        write("ERROR: " + str);
    }
}
