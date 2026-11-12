public class Main {
    /**
     * Starts the Reed-Muller code generator.
     *
     * @param args command-line arguments, not used
     * @throws Exception if starting the user interface fails
     */
    public static void main(String[] args) throws Exception {
        UserInterface.getInstance().loop();
    }
}
