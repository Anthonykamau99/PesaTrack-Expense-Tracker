# PesaTrack – GitHub Collaboration Plan (6 Members)

This document explains how the 6 group members should work so that **every member has clear, meaningful GitHub contributions**.

---

## Recommended Ownership Split

| Member | Role / Ownership Area | Files / Folders Owned | Key Responsibility |
|--------|-----------------------|-----------------------|--------------------|
| **Member 1** | Project Setup + Data Models | `build.gradle.kts`, `settings.gradle.kts`, `gradle.properties`, `app/build.gradle.kts`, `AndroidManifest.xml`, `data/local/Expense.kt`, `data/local/Budget.kt`, `data/local/AppDatabase.kt`, `data/local/ExpenseDao.kt`, `data/local/BudgetDao.kt`, resource values (`colors.xml`, `strings.xml`, `themes.xml`, launcher icons) | Create the project structure, entities, Room database & DAOs |
| **Member 2** | Repository Layer | `data/repository/ExpenseRepository.kt` | Implement the single source of truth between ViewModels and database (CRUD methods) |
| **Member 3** | ViewModels | `ui/dashboard/DashboardViewModel.kt`, `ui/expense/ExpenseViewModel.kt`, `ui/budget/BudgetViewModel.kt` | Business logic & data exposure for the UI |
| **Member 4** | Dashboard UI + Navigation | `ui/dashboard/DashboardActivity.kt`, `res/layout/activity_dashboard.xml` | Main screen, navigation to other screens, budget summary display |
| **Member 5** | Expense CRUD UI | `ui/expense/AddEditExpenseActivity.kt`, `ui/expense/ExpenseHistoryActivity.kt`, `ui/expense/ExpenseAdapter.kt`, `res/layout/activity_add_edit_expense.xml`, `res/layout/activity_expense_history.xml`, `res/layout/item_expense.xml` | Full Create / Read / Update / Delete expense screens + RecyclerView |
| **Member 6** | Budget UI + Background Work + Docs | `ui/budget/BudgetActivity.kt`, `res/layout/activity_budget.xml`, `worker/BudgetReminderWorker.kt`, `util/ReminderScheduler.kt`, `README.md`, `ic_notification.xml` | Budget screen, WorkManager daily reminder, final documentation |

---

## How to Create Genuine Contribution History

### Step 1 – One person creates the GitHub repository
1. Create a **new private/public repository** on GitHub (e.g. `PesaTrack-BBIT`).
2. Do **not** upload the full finished project yet.
3. Clone it locally:
   ```bash
   git clone https://github.com/YOUR_GROUP/PesaTrack-BBIT.git
   cd PesaTrack-BBIT
   ```

### Step 2 – Member 1 creates the base project structure
Member 1 should:
- Create the empty Android project (or push the skeleton)
- Add the package structure
- Commit the Gradle files + empty packages + entities + database
- Push to `main`

Example commit messages for Member 1:
```
feat: initialize Android project with Kotlin and Room
feat: add Expense and Budget entities
feat: create Room database and DAOs
```

### Step 3 – Each member creates their own branch
Every member works on a **feature branch**:

```bash
# Example for Member 5
git checkout main
git pull
git checkout -b feature/member5-expense-ui
```

Suggested branch names:
- `feature/member1-data-layer`
- `feature/member2-repository`
- `feature/member3-viewmodels`
- `feature/member4-dashboard`
- `feature/member5-expense-crud`
- `feature/member6-budget-workmanager`

### Step 4 – Add only your files and commit meaningfully
Each member should only touch the files they own (or make small related improvements).

Good commit messages:
```
feat: implement ExpenseRepository with full CRUD
feat: add DashboardActivity with live budget summary
feat: create Add/Edit Expense screen with validation
feat: implement ExpenseHistory with RecyclerView
feat: add Budget screen and financial tips
feat: schedule daily budget reminder with WorkManager
docs: update README with project overview
```

### Step 5 – Push and create Pull Request
```bash
git push -u origin feature/member5-expense-ui
```
Then open a **Pull Request** on GitHub into `main`.  
Other members can review. Merge after review.

### Step 6 – Everyone contributes
- Use **Issues** for tasks (optional but good)
- At least 3–5 meaningful commits per person
- Never force-push or rewrite history after others have pulled

---

## Quick Start Commands (for each member)

```bash
# 1. Clone (only once)
git clone https://github.com/YOUR_GROUP/PesaTrack-BBIT.git
cd PesaTrack-BBIT

# 2. Create your branch
git checkout -b feature/memberX-your-area

# 3. Copy/add only your assigned files
# (or edit them)

# 4. Stage and commit
git add path/to/your/files
git commit -m "feat: clear description of what you did"

# 5. Push
git push -u origin feature/memberX-your-area
```

Then create a Pull Request on GitHub.

---

## Important Rules (from the CAT brief)
- Every member must have **visible commits** and contributions.
- Simply uploading the finished project at the end is **not** accepted.
- You must be able to explain the code you committed.
- Prefer smaller, functional, well-understood code over a large unfinished app.

---

## Suggested Order of Work
1. Member 1 → base structure + data layer
2. Member 2 → repository (depends on Member 1)
3. Member 3 → ViewModels (depends on Member 2)
4. Member 4 + Member 5 → UI screens (can work in parallel after ViewModels)
5. Member 6 → Budget UI + WorkManager + final README polish

After all PRs are merged, the project will be complete and every member will have clear contribution history.
