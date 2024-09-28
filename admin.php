<?php
@include 'connect.php';
session_start();

if (!isset($_SESSION['admin_name'])) {
    header('location:register.php');
}
?>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - PowerSphere</title>
    <link rel="stylesheet" href="admins.css"> <!-- Link to your admin CSS -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;600&display=swap" rel="stylesheet">
</head>
<body>
  <header  class="main-header">
        <h1>PowerSphere</h1>
    </header>
    <header class="sub-header">
        <nav>
            <ul>
		<li><a href="start.php">HOME</a></li>
		<li><a href="#about">BILLING</a></li>
		<li><a href="#about">PAYMENT</a></li>                     
                <li><a href="#contact">CONTACT</a></li> 
		<li><a href="#about">ABOUT</a></li>
            </ul>
        </nav>
    </header>
      <div class="start">

         <p>We're glad to have you back. Here’s a quick overview of the system:</p>
     </div>
      
		
    <main>
        <h1>Facilities</h1>
        <div class="fac-main">
        <section id="user-management">
            <h2>User Management</h2>
            <button onclick="addUser()">Add New User</button>
      
        </section>

        <section id="billing-management">
            <h2>Billing Management</h2>
            <button onclick="generateInvoice()">Generate Invoice</button>
           
        </section>

        <section id="reports">
            <h2>Reports</h2>
            <button onclick="viewReports()">View Reports</button>
            <div>
                <!-- Report summaries will go here -->
            </div>
        </section>
	</div>
 </main>
        <div class="settings">
            <h2>Settings</h2>
            <form>
                <label for="billing-cycle">Billing Cycle:</label>
                <input type="text" id="billing-cycle" name="billing-cycle">
                <button type="submit">Save Settings</button>
            </form>
        </div>
   

    <footer>
        <h2>&copy; 2024 PowerSphere. All rights reserved.</h2>
    </footer>

    <script>
        function addUser() {
          
            alert('Add User functionality to be implemented.');
        }
        
        function generateInvoice() {
            
            alert('Generate Invoice functionality to be implemented.');
        }
        
        function viewReports() {
            
            alert('View Reports functionality to be implemented.');
        }
    </script>
</body>
</html>

