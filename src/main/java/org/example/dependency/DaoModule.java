package org.example.dependency;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import dagger.Module;
import dagger.Provides;
import org.example.dynamodb.EmployeeCredentialsDao;
import org.example.dynamodb.EmployeeDao;
import org.example.dynamodb.TimeEntryDao;

import javax.inject.Singleton;

@Module
public class DaoModule {

    @Provides
    @Singleton
    public AmazonDynamoDB provideAmazonDynamoDB() {
        return AmazonDynamoDBClientBuilder.defaultClient();
    }

    @Provides
    @Singleton
    public DynamoDBMapper provideDynamoDBMapper(AmazonDynamoDB amazonDynamoDB) {
        return new DynamoDBMapper(amazonDynamoDB);
    }

    @Provides
    @Singleton
    public EmployeeCredentialsDao provideEmployeeCredentialsDao(DynamoDBMapper dynamoDBMapper) {
        return new EmployeeCredentialsDao(dynamoDBMapper);
    }

    @Provides
    @Singleton
    public EmployeeDao provideEmployeeDao(DynamoDBMapper dynamoDBMapper) {
        return new EmployeeDao(dynamoDBMapper);
    }

    @Provides
    @Singleton
    public TimeEntryDao provideTimeEntryDao(DynamoDBMapper dynamoDBMapper) {
        return new TimeEntryDao(dynamoDBMapper);
    }
}
