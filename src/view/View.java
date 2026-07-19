package view;


public interface View {
    void show(String message);

    String getString(String prompt);

    int getInteger(String prompt);

    double getDouble(String prompt);
}
