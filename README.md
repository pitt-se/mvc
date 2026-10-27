# A2: Refactor the Kitchen (MVC)

> **Closed to genAI for this assignment!** (see the syllabus for the full policy)
> *~1.5h time estimate · 10 pts*

## Learning Goals
- **Review** code where data, display, and user input are mixed, and explain why that design makes changes hard
- **Refactor** a small app into Model, View, and Controller

## Refactor the Kitchen

See *Cook N'At* from A1.
Assume someone on the CloudCuisine team already built a first version of the kitchen dashboard. It works, but everything is in one file: data, rules, HTML, & button handlers. Currently, every new feature requires changes in a lot of places.

Your task is to refactor and improve it using the **Model-View-Controller (MVC)** architectural style.

## Overview: MVC

Please complete this reading before continuing: https://developer.mozilla.org/en-US/docs/Glossary/MVC
Optional: https://martinfowler.com/eaaDev/SeparatedPresentation.html 

| Part | Main Task | What it should not do |
| ---- | ------------ | --------------------- |
| **Model** | Holds data and rules (e.g., "the oven temperature must be between 170 and 550 F") | Handle the screen or the web (no HTML, no `HttpExchange`) |
| **View** | Presents the data to the user | Change the data or contain rules |
| **Controller** | Handles user input (requests, form values) and tells the model what to do | Hold the data or build HTML |

When the model changes (e.g., the oven finished pre-heating), the view needs to update. In a web app: the view builds a new page from the current model for every request (the page in this app reloads itself every second).

## Repository Structure

| Folder | Contents |
| ------ | -------- |
| `part1/` | `Kitchen.java`, the original app (Part 1) |
| `cooknat/` | skeleton for Parts 2 and 3a: `KitchenModel`, `KitchenView`, `KitchenController`, `KitchenApp`, `KitchenConsole` |
| `.vscode/` | Run configurations "Part 1: Kitchen" and "Part 2/3: KitchenApp" |

## Setup

1. Clone this repository (or *Code → Download ZIP*) and open the folder in VS Code (requires >= Java 17)
2. Run part 1 (`java Kitchen.java` in the `part1` folder), and open http://localhost:1530 in your browser
3. Try it out: turn the oven on and wait a few seconds, set a temperature, start the dishwasher, turn off the fridge, and use "Turn everything off"
4. Review the code. You can skim the HTML and CSS, you do not need to change them.

*Note: You do not need to know a web framework or HTML. The app uses a small web server (`com.sun.net.httpserver`). The starter code includes everything you need.*

*Note: If you see "Address already in use", another version of the app (e.g., `Kitchen.java`) is still running.*

---

## Part 1 - New Feature (2 pts)

Before you refactor anything, make one change to the original code.

**New requirement:** The oven and the dishwasher share a power circuit and cannot both be on at the same time. If a user tries to turn one on while the other is already on, nothing should turn on and a notification should inform the user (notifications list).

1. `TODO 1.1`: Implement this requirement in `Kitchen.java` and list the line number(s) you changed or added (in your Part 1 version).
2. `TODO 1.2`: Find **1 more place** in `Kitchen.java` where the data, rules, display, or user input are mixed together and make the code hard to change (e.g., a method with more than one responsibility). State the line number(s) in the **original** `Kitchen.java` and explain in 1-2 sentences **what would go wrong** if you had to change it.

For example: *"If the maximum oven temperature changes to 500 F, I have to change two places (ln:XXX and ln:YYY). If I forget one, the error message no longer matches the check."* (This example does not count!)

*Note: Your Part 1 version stays in `part1/`, you will submit it as well.*

## Part 2 - Refactoring (4 pts)

Move the code of your Part 1 version of `Kitchen.java` (incl. the new rule) into the skeleton in `cooknat/`. The skeleton has four classes and already contains the method signatures. `KitchenView` and `KitchenApp` are already done (no need to change anything here). Your task is to move the data and rules into `KitchenModel` and the request handling into `KitchenController` (complete `TODO 2.1` to `TODO 2.5` in order):

* `KitchenModel.java`: appliances, notifications, timers, rules
* `KitchenView.java`: everything that builds the HTML (no need to change anything, please take a look at how `page()` reads data)
* `KitchenController.java`: handles the requests, reads the form values, and calls the model and the view (see helper methods for sending pages and redirects)
* `KitchenApp.java`: `main`, creates the model, view, and controller and starts the server (no need to change anything here)

*Note: You may add private fields and helper methods. If you change a method signature, briefly explain why in your answers.*

Your app must **behave exactly as before**. Please make sure that:

* [ ] `KitchenModel` contains no HTML and does not use `HttpExchange`
* [ ] `KitchenController` contains no HTML and no rules
* [ ] The pre-heat and "dishwasher finished" notifications still show up on their own
* [ ] "Part 2/3: KitchenApp" runs in VS Code, and `javac *.java` and `java KitchenApp` work in the `cooknat` folder (no libraries or build tools)

**Make sure that this still works**:

1. Turn the oven on and wait 2-3 seconds: "Oven pre-heated to 350 F" should show up as a notification
2. Set the temperature for the oven (> 550): an error message should appear
3. Turn the dishwasher on while the oven is on: nothing should turn on, a notification should appear to inform the user (see Part 1)
4. Turn the oven off, start the dishwasher, and try to change the cycle: an error message should appear. Click OK; after 3 seconds, the dishwasher should finish its cycle
5. Set the dishwasher cycle to "Eco" and the temperature to 400 F: both should show up on the page and in the notifications
6. Turn off the fridge: should show a confirmation page (cancel does not turn it off)
7. Turn the oven on and use "Turn everything off": oven turns off, fridge stays on

*Hint: Do not rewrite everything at once. Approach one TODO at a time, then restart the app, make sure that it still works, then move on to the next one, etc.*

**Design decision (`TODO 2.5`):** Decide where the rule "turning off the fridge needs a confirmation" belongs (e.g., in `KitchenModel` or in `KitchenController`), then explain your choice in 2-3 sentences.

## Part 3 - Extensions (4 pts)

### 3a. New View (2.5 pts)

Add a console version of the kitchen: `KitchenConsole.java` reads commands in the terminal and uses the **same model** as the web page. Support at least:

* `status`: shows all appliances and the last notification (already complete)
* `on <appliance>` and `off <appliance>` (e.g., `on oven`); turning off the fridge still asks for confirmation
* `temp <value>` (e.g., `temp 400`)

Start the console in `main` (`TODO 3a.1`, e.g., `new Thread(new KitchenConsole(model)).start();`), type `on oven` in the terminal, and watch the browser. The oven turns on there too!

*This change should not touch `KitchenModel.java` at all. You only complete `KitchenConsole.java` (`TODO 3a.2` and `TODO 3a.3`) and add one line in `KitchenApp.java`. (`KitchenConsole` is a small view and controller for the terminal in one class, that is ok for this assignment.)*

Did your design decision from Part 2 (where the fridge confirmation lives) make the console easier or harder to write? Explain in 1-2 sentences.

### 3b. New Feature (1.5 pts)

Pick **one** feature (no need to implement it):

* **Child safety:** The oven can be locked (through the webapp). A locked oven cannot be turned on and its temperature cannot be changed.
* **Auto-off:** The oven turns itself off after 30 seconds (we pretend it is 3 hours), a notification informs the user
* **Air fryer:** A new appliance with its own temperature range (180-400 F), with similar status notifications as for oven, including a "done" notification
* **Your own idea** (similar type as above)

Briefly explain which files you would need to change and what those changes are. Then compare: how many places would the same feature need to touch in the original `Kitchen.java`?

## Submission

`.zip` your files and upload them to Gradescope. Make sure you include all:

* Your Part 1 version of `Kitchen.java`
* Your code from Part 3: `KitchenModel.java`, `KitchenView.java`, `KitchenController.java`, `KitchenConsole.java`, `KitchenApp.java`
* Your text answers to Parts 1, 2 (design decision), 3a (design decision), and 3b, either in the Gradescope text fields or as one file (`.md`, `.txt`, or `.pdf`)

*Please make sure your app compiles and runs with `javac *.java` and `java KitchenApp` (in the `cooknat` folder). If it does not compile/run, points may be taken off.*

If unsure, please reach out!
