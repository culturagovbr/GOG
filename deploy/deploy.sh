sudo /etc/init.d/jboss stop
sudo rm -rf /opt/GOG/target/*
sudo rm -rf /opt/jboss-as-7.1.1.Final/standalone/deployments/*
cd /opt/GOG/
mvn package
sudo cp /opt/GOG/target/GOG.war /opt/jboss-as-7.1.1.Final/standalone/deployments/
sudo /etc/init.d/jboss start

