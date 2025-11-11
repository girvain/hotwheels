# hotwheels

An example application built from the [ce3.g8 template](https://github.com/typelevel/ce3.g8).

## Run application

```shell
sbt run
```

## Build Docker image

```
sbt docker:publishLocal
```
## Run tests

```shell
sbt test
```

## ssh into Docker db container

```shell
docker exec -it hotwheels-postgres-1 psql -U postgres -d hotwheels_store 
```

## sbt-tpolecat

This template uses the `sbt-tpolecat` sbt plugin to set Scala compiler options to recommended defaults. If you want to change these defaults or find out about the different modes the plugin can operate in you can find out [here](https://github.com/typelevel/sbt-tpolecat/).

##  For using as a template
- [ ] change the tables.sql
- [ ] change the name and Docker / packageName in build.sbt
- [ ] change the image name in app/docker-compose.yml
- [ ] change the env variables in docker-compose.yml for your schema
- [ ] change the config/Config.scala to match your env variables (db name)
I think that's it?