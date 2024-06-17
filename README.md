# IES_Point_of_Sale
### IES Project

**Name:**  Interactive Earnings and Sale Software (IESS)

## Project theme  :  Point-of-Sale

## Members

- Team Manager : Gabriel Teixeira
- Product Owner Gonçalo Abrantes
- Architect: Marcos Miguélez
- DevOps Master : Rafael kauati

# About the source code on the repository :

* Even though the main has the source code of the actual project, we highly recomend the use of the source code from the "dev" branch, which was more tested
  
* Regarding local tests of the project, we recommend the use of the run-compose file, that can be found in the root of the project, that is bash automation file to
run automacally build and run the docker images
  ```bash run-compose```

  Note : When you firstly run the run-compose file(e.g : after you clone the repository on your machine), its quite possible to receive an error message regarding the jpa entity manager, this occurs because the data base volume/image was not properly create (yet)
  If this error occurs just kill de compose process and rerun run-compose


#  Base user stories/epícs : 

**Epic 1 : Inventory Management**

+ As Alex, I should be able to add and update products (and their information) in stock.

**Epic 2 : Up to date Product Information (update pricing based on the stock market)**

+ As Sarah, I want to view detailed product information, including current stock, pricing, and
any ongoing promotions, to provide accurate information to customers and prevent selling
products that are out of stock.

**Epic 3 : Streamlined Checkout Process**

+ As Sarah, I want to be able to search for products and to add them to the customer’s cart with one click and confirm their payment efficiently, to ensure a smooth and pleasant checkout experience for every customer.

**Epic 4 : Inventory supervision**

+ As Alex,I want to receive automatic alerts when popular products reach a defined low-stock threshold, and I want to initiate restocking requests directly from the system.

**Epic 5 : Sales Analytics Dashboard**

+ As Alex, I want a dashboard displaying sales data with filters for different time periods, allowing me to view sales figures, compare performance, and identify trends.

**Epic 6 : Employee Performance Tracking**

+ As Alex, I want to access reports on cashier performance, including the number of transactions processed, transaction accuracy, and any errors or discrepancies in transactions.


## Architeture

![Arquitetura](https://github.com/SilentStorm52/IES_Point_of_Sale/assets/107052640/acdf8e32-04ee-44c8-bdf4-527b9e144b16)

+ **Data generation and Middleware data queues:** A simulated sensor that simulates sales performed by a certain employee identified by his id and products (these data come from the api)

+ **Database layer:** A single Architecture to store data for and return data through message queues for the server-side application  mysql (which was chosen by its simplicity, stability and wide community for features support)



 + **API layer:**  Multi-layered Architecture service that, in a scope of a single service/application, will handle the communication of the server application with the client-side application, bizz logic, data access and any other type of logical configuration of the project(

+ **Client layer:** A Progressive Web Application (PWA) for implemented as a user web Interface developed with react library, which is a pretty popular, widely known (with tons of plugins and other tools for development) and powerful lib to build single-page dynamic web app, which is the case for this project. This service for itself will provide the login (authenticated) for the user and provide visual access to the data of the stock, fetching from the API layer
  
## Domain diagram

![Domain_diagram](https://github.com/SilentStorm52/IES_Point_of_Sale/assets/107052640/3b53ed42-654d-4817-98ed-f1a13c47ef34)


## Sequence Diagram

![Sequence_Diagram](https://github.com/SilentStorm52/IES_Point_of_Sale/assets/107052640/40184509-38ad-4d3a-b422-d25137292acd)


### Link for server: http://deti-ies-13.ua.pt/
Note : due to some issues on the development the application on the vm is not avaiable to access, still you can test the project source code cloning this repository

*Access credentials for Maneger application main account:*

Username: ```alexj@gmail.com```

Password: ```alexiano```


*Access credentials for Employee application main account:*

Username: ```lilsarah@gmail.com```

Password: ```secretpassword```




