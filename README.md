# Household Management Web Application

A web application designed to help roommates manage household tasks efficiently. The application enables users to create households, assign tasks, track progress, and share responsibilities.

## Features
- **User Management**: Users can join a household, view their assigned tasks, and track the completion status of other household members' tasks.
- **Task Assignment**: Allows for weekly task assignments to each household member, with real-time updates using AJAX.
- **Task Tracking**: Members can mark tasks as completed, and view the overall progress of all household tasks.
- **Database Integration**: Utilizes MySQL for storing user and household data, including tasks and completion status.
  
## Technologies Used
- **Backend**: Java Servlets, JDBC
- **Frontend**: HTML, CSS, JavaScript, AJAX for dynamic content updates
- **Database**: MySQL for data storage and retrieval
- **Tools**: Git for version control, Eclipse for development

## Setup Instructions

To run this project locally, follow these steps:

1. **Clone the Repository**:
   ```bash
   git clone git@github.com:seojoonl/household-management-webapp.git
   ```

2. **Import the Project into Eclipse**:
   - Open Eclipse and go to **File > Import > Existing Projects into Workspace**.
   - Select the project directory and click **Finish**.

3. **Set Up the Database**:
   - Create a MySQL database and import the provided SQL script (if any) to create the necessary tables.
   - Update the database connection settings in the Java Servlet files (e.g., `DBConnection.java`):
     ```java
     String dbUrl = "jdbc:mysql://localhost:3306/your_database_name";
     String dbUser = "your_db_user";
     String dbPassword = "your_db_password";
     ```

4. **Run the Application**:
   - Start the server (e.g., Apache Tomcat) in Eclipse.
   - Access the application at `http://localhost:8080/household-management-webapp`.

## Usage
- **Create a Household**: Register or join an existing household using the application.
- **Assign Tasks**: As a member, assign tasks to yourself or others for the week.
- **Track Progress**: Check off tasks as completed and monitor the progress of others in the household.

## Contributing
Contributions are welcome! To contribute:
1. Fork the repository.
2. Create a new branch (`git checkout -b feature/YourFeature`).
3. Commit your changes (`git commit -m 'Add your feature'`).
4. Push the branch (`git push origin feature/YourFeature`).
5. Open a pull request.

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact
For any inquiries or issues, please reach out to:
- **Name**: SeoJoon Lee
- **Email**: seojoonl@usc.edu
- **LinkedIn**: [linkedin.com/in/seojoonlee](https://linkedin.com/in/seojoonlee)