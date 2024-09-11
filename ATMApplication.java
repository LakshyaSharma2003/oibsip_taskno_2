import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ATMApplication {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ATMInterface().createAndShowGUI());
    }
}

class ATMInterface {
    private JFrame frame;
    private JTextField userIdField;
    private JPasswordField pinField;
    private JTextArea transactionHistoryArea;
    private BankingOperations bankingOperations;
    private Account currentAccount;
    private JPanel mainPanel;
    private JPanel loginPanel;

    public void createAndShowGUI() {
        frame = new JFrame("ATM Interface");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(30, 30, 30)); // Dark background color

        showLoginScreen();
        frame.setVisible(true);
    }

    private void showLoginScreen() {
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());

        loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(new Color(40, 40, 40)); // Darker background for login
        loginPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        // Heading for the login screen
        JLabel headingLabel = new JLabel("LAKSHYA BANK OF MONEY", JLabel.CENTER);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 20)); // Adjust font size
        headingLabel.setForeground(new Color(0, 123, 255)); // Bright color for visibility
        headingLabel.setBorder(new EmptyBorder(0, 0, 20, 0)); // Padding below heading
        loginPanel.add(headingLabel, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 1; // Start adding login components below the heading

        JLabel userIdLabel = new JLabel("User ID:");
        userIdLabel.setForeground(Color.WHITE);
        loginPanel.add(userIdLabel, gbc);

        userIdField = new JTextField(15);
        gbc.gridx = 1;
        loginPanel.add(userIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel pinLabel = new JLabel("PIN:");
        pinLabel.setForeground(Color.WHITE);
        loginPanel.add(pinLabel, gbc);

        pinField = new JPasswordField(15);
        gbc.gridx = 1;
        loginPanel.add(pinField, gbc);

        JButton loginButton = createStyledButton("Login");
        gbc.gridx = 1;
        gbc.gridy = 3;
        loginPanel.add(loginButton, gbc);

        frame.add(loginPanel, BorderLayout.CENTER);

        loginButton.addActionListener(e -> handleLogin());

        frame.revalidate();
        frame.repaint();
    }

    private void handleLogin() {
        String userId = userIdField.getText();
        String pin = new String(pinField.getPassword());
        bankingOperations = new BankingOperations();
        currentAccount = bankingOperations.authenticate(userId, pin);

        if (currentAccount != null) {
            animateFadeTransition(loginPanel, this::showMainMenu);
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid credentials, please try again.");
        }
    }

    private void animateFadeTransition(JPanel panelToFade, Runnable afterFade) {
        Timer fadeTimer = new Timer(30, new ActionListener() {
            private float opacity = 1.0f;

            @Override
            public void actionPerformed(ActionEvent e) {
                opacity -= 0.05f;
                if (opacity <= 0) {
                    ((Timer) e.getSource()).stop();
                    afterFade.run();
                    panelToFade.setOpaque(false);
                    panelToFade.repaint();
                    frame.repaint();
                } else {
                    panelToFade.setOpaque(true);
                    panelToFade.repaint();
                    frame.repaint();
                }
            }
        });
        fadeTimer.start();
    }

    private void showMainMenu() {
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());

        JPanel mainMenuPanel = new JPanel(new BorderLayout());
        mainMenuPanel.setBackground(new Color(60, 60, 60)); // Background for menu

        // Welcome message
        JLabel welcomeLabel = new JLabel("Welcome to LAKSHYA BANK OF MONEY ATM", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(0, 123, 255)); // Bright color for visibility
        welcomeLabel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainMenuPanel.add(welcomeLabel, BorderLayout.NORTH);

        JPanel menuPanel = new JPanel(new GridLayout(6, 1, 5, 5));
        menuPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        menuPanel.setBackground(new Color(60, 60, 60)); // Background for menu

        JButton balanceButton = createStyledButton("Check Balance");
        JButton transactionHistoryButton = createStyledButton("Transaction History");
        JButton withdrawButton = createStyledButton("Withdraw");
        JButton depositButton = createStyledButton("Deposit");
        JButton transferButton = createStyledButton("Transfer");
        JButton quitButton = createStyledButton("Quit");

        menuPanel.add(balanceButton);
        menuPanel.add(transactionHistoryButton);
        menuPanel.add(withdrawButton);
        menuPanel.add(depositButton);
        menuPanel.add(transferButton);
        menuPanel.add(quitButton);

        mainMenuPanel.add(menuPanel, BorderLayout.CENTER);
        frame.add(mainMenuPanel, BorderLayout.CENTER);

        balanceButton.addActionListener(e -> showBalance());
        transactionHistoryButton.addActionListener(e -> showTransactionHistory());
        withdrawButton.addActionListener(e -> handleWithdraw());
        depositButton.addActionListener(e -> handleDeposit());
        transferButton.addActionListener(e -> handleTransfer());
        quitButton.addActionListener(e -> frame.dispose());

        frame.revalidate();
        frame.repaint();
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(0, 123, 255)); // Primary color
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new LineBorder(new Color(0, 95, 200), 2));
        button.setPreferredSize(new Dimension(200, 50));
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setOpaque(true);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                animateButtonHover(button, new Color(0, 123, 255), new Color(0, 95, 200));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                animateButtonHover(button, new Color(0, 95, 200), new Color(0, 123, 255));
            }
        });

        return button;
    }

    private void animateButtonHover(JButton button, Color fromColor, Color toColor) {
        Timer timer = new Timer(10, new ActionListener() {
            private float progress = 0.0f;
            private final float increment = 0.1f;

            @Override
            public void actionPerformed(ActionEvent e) {
                progress += increment;
                if (progress > 1.0f) {
                    progress = 1.0f;
                    ((Timer) e.getSource()).stop();
                }
                Color newColor = blendColors(fromColor, toColor, progress);
                button.setBackground(newColor);
            }
        });
        timer.start();
    }

    private Color blendColors(Color color1, Color color2, float ratio) {
        int r = (int) (color1.getRed() * (1 - ratio) + color2.getRed() * ratio);
        int g = (int) (color1.getGreen() * (1 - ratio) + color2.getGreen() * ratio);
        int b = (int) (color1.getBlue() * (1 - ratio) + color2.getBlue() * ratio);
        return new Color(r, g, b);
    }

    private void showBalance() {
        JOptionPane.showMessageDialog(frame, "Current Balance: $" + currentAccount.getBalance());
    }

    private void showTransactionHistory() {
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());

        JPanel historyPanel = new JPanel(new BorderLayout());
        transactionHistoryArea = new JTextArea(15, 40);
        transactionHistoryArea.setEditable(false);
        transactionHistoryArea.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE), "Transaction History", TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 14), Color.WHITE));
        transactionHistoryArea.setBackground(new Color(50, 50, 50)); // Dark background for text area
        transactionHistoryArea.setForeground(Color.WHITE); // White text for better contrast
        JScrollPane scrollPane = new JScrollPane(transactionHistoryArea);
        historyPanel.add(scrollPane, BorderLayout.CENTER);

        JButton backButton = createStyledButton("Back");
        historyPanel.add(backButton, BorderLayout.SOUTH);

        // Load and display transaction history
        transactionHistoryArea.setText(bankingOperations.getTransactionHistory(currentAccount));

        backButton.addActionListener(e -> animateFadeTransition(historyPanel, this::showMainMenu));

        frame.add(historyPanel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }

    private void handleWithdraw() {
        String amountStr = JOptionPane.showInputDialog(frame, "Enter amount to withdraw:");
        if (amountStr != null) {
            double amount = Double.parseDouble(amountStr);
            bankingOperations.withdraw(currentAccount, amount);
            showBalance();
        }
    }

    private void handleDeposit() {
        String amountStr = JOptionPane.showInputDialog(frame, "Enter amount to deposit:");
        if (amountStr != null) {
            double amount = Double.parseDouble(amountStr);
            bankingOperations.deposit(currentAccount, amount);
            showBalance();
        }
    }

    private void handleTransfer() {
        String recipientId = JOptionPane.showInputDialog(frame, "Enter recipient's User ID:");
        String amountStr = JOptionPane.showInputDialog(frame, "Enter amount to transfer:");
        if (recipientId != null && amountStr != null) {
            double amount = Double.parseDouble(amountStr);
            bankingOperations.transfer(currentAccount, recipientId, amount);
            showBalance();
        }
    }
}

class BankingOperations {
    private TransactionHistory transactionHistory = new TransactionHistory();

    public Account authenticate(String userId, String pin) {
        // Authentication logic
        return new Account(userId, 1000); // Dummy account
    }

    public void withdraw(Account account, double amount) {
        // Withdrawal logic
        account.setBalance(account.getBalance() - amount);
        transactionHistory.addTransaction("Withdrawn: $" + amount);
    }

    public void deposit(Account account, double amount) {
        // Deposit logic
        account.setBalance(account.getBalance() + amount);
        transactionHistory.addTransaction("Deposited: $" + amount);
    }

    public void transfer(Account account, String recipientId, double amount) {
        // Transfer logic
        account.setBalance(account.getBalance() - amount);
        transactionHistory.addTransaction("Transferred: $" + amount + " to " + recipientId);
    }

    public String getTransactionHistory(Account account) {
        return transactionHistory.getHistory();
    }
}

class Account {
    private String userId;
    private double balance;

    public Account(String userId, double balance) {
        this.userId = userId;
        this.balance = balance;
    }

    public String getUserId() {
        return userId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}

class TransactionHistory {
    private List<String> transactions = new ArrayList<>();

    public void addTransaction(String transaction) {
        transactions.add(transaction);
    }

    public String getHistory() {
        StringBuilder history = new StringBuilder();
        for (String transaction : transactions) {
            history.append(transaction).append("\n");
        }
        return history.toString();
    }
}
    