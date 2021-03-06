# Dropwizard Todo Rest Service

### Getting started

Start the application by running:

```bash
mvn clean package -DskipTests
```

If you want to automatically spin up a MongoDB for testing, use the following command:

```bash
mvn clean package -DskipTests -DembeddedMongo
```

Run test with Maven:

```bash
mvn test
```
   
   
---


### Configuration

By editing `./configuration.yml`, you can change the storage backend. By setting option `storage` to `memory`, 
an in-memory storage will be used; By setting `storage` to `mongodb`, a MongoDB instance will be used, with its endpoint
specified in the following fields:
- `mongohost`
- `mongoport`
- `mongodb`