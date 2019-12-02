package dhbw.columnbus;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptorBuilder;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.TableDescriptor;
import org.apache.hadoop.hbase.client.TableDescriptorBuilder;

public class Main
{

    public static void main(String[] args)
        throws IOException
    {
        Configuration config = HBaseConfiguration.create();
        config.clear();
        config.set("hbase.zookeeper.quorum", "columnbus-hbase");
        config.set("hbase.zookeeper.property.clientPort", "2181");

        config.set("hbase.master.port", "16000");
        config.set("hbase.master.info.port", "16010");

        config.set("hbase.regionserver.port", "16020");
        config.set("hbase.regionserver.info.port", "16010");

        config.set("hbase.localcluster.port.ephemeral", "false");

        HBaseAdmin.available(config);

        try (Connection connection = ConnectionFactory.createConnection(config);
                Admin admin = connection.getAdmin();) {
            TableName tableName = TableName.valueOf("customer");
            if (!admin.tableExists(tableName)) {
                TableDescriptor htable = TableDescriptorBuilder.newBuilder(tableName)
                    .setColumnFamily(ColumnFamilyDescriptorBuilder.of("personal"))
                    .setColumnFamily(ColumnFamilyDescriptorBuilder.of("address"))
                    .build();
                admin.createTable(htable);
                System.out.println("customer table was created");
            }
            else {
                System.out.println("customer table already exists");
            }
        }
    }
}
