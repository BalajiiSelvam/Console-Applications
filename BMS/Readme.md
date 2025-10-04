Classes
-------
1. Account
Variables :
	|-- accountNumber
	|-- name
	|-- type ( Savings, current, Admin )
	|-- contact
	|-- pin
	|-- balance
	|-- isAdmin
Constructor : ( Parameterized )
toString() : ( Overriden )
2. Transaction
Variables : 
	|-- accountNumber
	|-- type ( Deposit, Withdraw, Transfer )
	|-- balanceAfter
	|-- amount
	|-- timeStamp
Constructor : ( Parameterixed )
toString() : ( Overriden )
3. Bank
Variables : 
	|-- Scanner sc
	|-- List<Account> accounts
	|-- List<Transaction> transactions
	|-- Map<String, OtpDetails> otpMapping
Methods : 
	|-- openAccount()
	|-- generateAccNumber() -- ( Helper method )
	|-- deposit()
	|-- withdraw()
	|-- transfer()
	|-- findAccount(), verifyPIN(), generaterOTP(), verifyOTP() -- ( Helper method )
	|-- checkBalance()
	|-- viewTransactionHistory()
	|-- downloadStatement()
	|-- exit()
4. BMS
Variables : 
	|-- Bank b
	|-- Scanner sc
Methods :
	|-- main()
5. OtpDetails
Variables :
	|-- otp
	|-- expiry time
Constructor : ( Parameterized )
