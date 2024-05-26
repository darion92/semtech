# semtech
Here's the instruction to run the application :
1. Clone the project and Build Docker image :
`docker build -t app/semtech . `
 
  
2. Run the container : 
`docker run -d -p 8080:8080 app/semtech`

   
3. Upload the file :


![Screenshot from 2024-05-26 10-21-47](https://github.com/darion92/semtech/assets/34867909/312d710b-ac61-4263-9ee1-333bf8216e2d)


# Hosting the service :
There's many solutions to host the docker container like : aws ecs, GCP runner, hostinger etc.
