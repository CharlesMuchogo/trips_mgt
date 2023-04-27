
<!DOCTYPE html>
<html>
<body>
	<h1>M-Expense - Android App for Managing Travel Expenses</h1>
	<p>M-Expense is an Android app that allows users to add trips and their expenses, and store the data in a Room database. The app provides an easy-to-use interface for managing travel expenses, and allows users to upload all the data in the database at once for easy record keeping.</p>
	<h2>Features</h2>
	<ul>
		<li>Add and edit trips, including start and end dates, trip name, and destination.</li>
		<li>Add and edit expenses for each trip, including expense name, amount, and category.</li>
		<li>Store trip and expense data in a Room database.</li>
		<li>View a list of all trips, sorted by start date.</li>
		<li>View a list of all expenses for each trip, sorted by date.</li>
		<li>Upload all trip and expense data to a remote server for easy record keeping.</li>
	</ul>
	<h2>Dependencies</h2>
	<ul>
		<li>AndroidX AppCompat for UI components and compatibility with older versions of Android</li>
		<li>Room for local storage and persistence of trip and expense data</li>
		<li>Material Design Components for UI elements and styling</li>
		<li>Retrofit for handling network requests and uploading data to a remote server</li>
	</ul>
	<h2>Prerequisites</h2>
	<ul>
		<li>Android Studio 4.2 or later</li>
		<li>Android SDK 31 or later</li>
		<li>Java 8 or later</li>
	</ul>
	<h2>Usage</h2>
	<ol>
		<li>Clone or download the repository to your local machine.</li>
		<li>Open the project in Android Studio.</li>
		<li>Build and run the app on an emulator or physical device.</li>
	</ol>
	<h2>How it Works</h2>
	<ol>
		<li>The app uses Room to store and persist trip and expense data in a local database.</li>
		<li>The <code>Trip</code> and <code>Expense</code> classes represent the data model for trips and expenses, respectively.</li>
		<li>The <code>TripDao</code> and <code>ExpenseDao</code> interfaces define the database access methods for the <code>Trip</code> and <code>Expense</code> classes, respectively.</li>
		<li>The <code>TripViewModel</code> and <code>ExpenseViewModel</code> classes provide the UI with access to the <code>TripRepository</code> and <code>ExpenseRepository</code>, respectively, and handle updating the UI state based on changes to the data in the database.</li>
		<li>The UI is built using a combination of XML layout files and Java code, and includes activities, fragments, and RecyclerView adapters.</li>
		<li>The app uses Retrofit to handle uploading trip and expense data to a remote server, and includes a separate <code>UploadDataActivity</code> for this
