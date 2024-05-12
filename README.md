# CPSC 210 Term Project

## Expense Distribution Calculator

This expense calculator will generate a report that shows how much money is spent under different category after user enter multiple expenses.
The report will also keep track of remained budget under the budget that user has set. By see this qualitative report, the user will 
obtain a clear view of which category he/she is over spending, so that he/she will adjust the purchasing habit.

I designed this app because I think it is easy to keep an expense journal every day,
but it is difficult to see where the money actually goes. As a student who is on budget, 
it is crucial mindful about daily expenses and control our purchasing habit. And by categorize our expenses, it becomes easier to make adjustments.
This app is meanly for calculating **daily expenses**.
It is not only suitable for students, but also for anyone who want to keep track of their spending habit. 


### User story

- *As a user, I am able to add multiple expenses to an expense list to generate the report by setting amount, choosing default category provided by the app,
setting date, text-based descriptions.*
- *As a user, I am able to delete an exiting expense record in the expense list for the report.*
- *As a user , I am able to and see a summary of how much money is spent under different category in a bar chart.*
- *As a user, I am able to set a budget for the calculating the report, and see remained budget on the report.*
- *As a user, I am able to save the expense list and report, and store them into a text file by clicking the "save" button.*
- *As a user, I can start a new round of calculation by clicking "start new round" button.*
- *As a user, I can continue to modify budget or calculate from previous expense list by clicking "load list from last calculation" button.*

### Phase 4: Task 2

Sun Nov 21 23:01:35 PST 2021
A new round of calculation started.

Sun Nov 21 23:01:35 PST 2021
A single entry has been added : 2021,1,1 : amount ($) : 10.0, category :Food & Grocery, description: Tim Hortons

Sun Nov 21 23:01:35 PST 2021
Food & Grocery's amount has increased to: $10.0

Sun Nov 21 23:01:35 PST 2021
A single entry has been added : 2021,2,2 : amount ($) : 10.0, category :Daily Necessities, description: KN95 Mask

Sun Nov 21 23:01:35 PST 2021
Daily Necessities's amount has increased to: $10.0

Sun Nov 21 23:01:35 PST 2021
Budget has been set to 50.0

Sun Nov 21 23:01:58 PST 2021
A single entry has been added : 2021,3,3 : amount ($) : 35.0, category :Others, description: vodka

Sun Nov 21 23:01:58 PST 2021
Others's amount has increased to: $35.0

Sun Nov 21 23:02:00 PST 2021
A single entry has been deleted : 2021,3,3 : amount ($) : 35.0, category :Others, description: vodka

Sun Nov 21 23:02:00 PST 2021
Others's amount has reduced to: $35.0

Sun Nov 21 23:02:05 PST 2021
Budget has been set to 50.0

Sun Nov 21 23:02:06 PST 2021
A report has been generated.

Sun Nov 21 23:02:06 PST 2021
Remained budget has been updated to  $30.0

Sun Nov 21 23:02:06 PST 2021
Total expense has been updated to $20.0

Sun Nov 21 23:02:07 PST 2021
User has saved the report


### Phase 4: Task 3

- Refactor a class that only represents a list of expense in model package that has method addExpense and deleteExpense; 
and a class that only represents operations on budget in model package that has method of setBudget and getBudget,
instead of integrated these methods in GenerateReport class
- Refactor a LoadData class in ui package that load report to JSON file.
- Set GenerateReport object to be protected field in ReportPanel, so that  
reportBarChart will not have association back to ExpenseCalculatorGUI. This will help to decrease coupling.
- Apply observer pattern,where GenerateReport class (or the ExpenseList class if I have more time to refactor) extends Observable, and ExpenseTable implements Observer. Once there is an expense added
or deleted, GenerateReport should notify the ExpenseTable, and ExpenseTable will update the user interface by adding rows or delete rows. 




