package com.rcn.order.common;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;


import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.internal.util.config.ConfigurationHelper;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.LongType;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Properties;

/**
 * The BatchIdPrefixSeqIdGenerator program that is maintained for the batch Id generation.
 *
 * @author GKC-Dev-AU-Team
 * @version 1.0
 * @since 2019-11-22
 */
public class FarmIdPrefixSeqIdGenerator extends SequenceStyleGenerator {

    private static final Logger logger = LoggerFactory.getLogger(FarmIdPrefixSeqIdGenerator.class);

    public static final String VALUE_PREFIX_PARAMETER = "valuePrefix";
    private static final String VALUE_PREFIX_DEFAULT = "";
    private String valuePrefix;

    public static final String NUMBER_FORMAT_PARAMETER = "numberFormat";
    private static final String NUMBER_FORMAT_DEFAULT = "%d";
    private String numberFormat;

    /**
     * This method returns the formatted batch request
     * @param session session details of the request
     * @param object object details
     * @return Returns the response after the lambda executed.
     **/
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        return valuePrefix + String.format(numberFormat, super.generate(session, object));
    }

    /**
     * This method returns the formatted batch request
     * @param type datatype
     * @param params param details
     * @param serviceRegistry service registry details
     **/
    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
        super.configure(LongType.INSTANCE, params, serviceRegistry);
        valuePrefix = ConfigurationHelper.getString(VALUE_PREFIX_PARAMETER,
                params, VALUE_PREFIX_DEFAULT);
        numberFormat = ConfigurationHelper.getString(NUMBER_FORMAT_PARAMETER,
                params, NUMBER_FORMAT_DEFAULT);
    }
}