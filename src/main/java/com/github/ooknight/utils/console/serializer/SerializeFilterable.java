package com.github.ooknight.utils.console.serializer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public abstract class SerializeFilterable {

    protected List<PropertyFilter> propertyFilters = null;
    protected List<ContextValueFilter> contextValueFilters = null;
    protected boolean writeDirect = true;

    public List<PropertyFilter> getPropertyFilters() {
        if (propertyFilters == null) {
            propertyFilters = new ArrayList<>();
            writeDirect = false;
        }
        return propertyFilters;
    }

    public List<ContextValueFilter> getContextValueFilters() {
        if (contextValueFilters == null) {
            contextValueFilters = new ArrayList<>();
            writeDirect = false;
        }
        return contextValueFilters;
    }

    public void addFilter(SerializeFilter filter) {
        if (filter == null) {
            return;
        }
        if (filter instanceof ContextValueFilter) {
            this.getContextValueFilters().add((ContextValueFilter) filter);
        }
        if (filter instanceof PropertyFilter) {
            this.getPropertyFilters().add((PropertyFilter) filter);
        }
    }

    public boolean apply(JSONSerializer jsonBeanDeser, Object object, String key, Object propertyValue) {
        if (jsonBeanDeser.propertyFilters != null) {
            for (PropertyFilter propertyFilter : jsonBeanDeser.propertyFilters) {
                if (!propertyFilter.apply(object, key, propertyValue)) {
                    return false;
                }
            }
        }
        if (this.propertyFilters != null) {
            for (PropertyFilter propertyFilter : this.propertyFilters) {
                if (!propertyFilter.apply(object, key, propertyValue)) {
                    return false;
                }
            }
        }
        return true;
    }

    protected Object processValue(JSONSerializer jsonBeanDeser, BeanContext beanContext, Object object, String key, Object propertyValue) {
        if (propertyValue != null) {
            if ((jsonBeanDeser.out.writeNonStringValueAsString
                    || (beanContext != null && (beanContext.getFeatures() & Feature.WRITE_NON_STRING_VALUE_AS_STRING.mask) != 0))
                    && (propertyValue instanceof Number || propertyValue instanceof Boolean)) {
                String format = null;
                if (propertyValue instanceof Number
                        && beanContext != null) {
                    format = beanContext.getFormat();
                }
                if (format != null) {
                    propertyValue = new DecimalFormat(format).format(propertyValue);
                } else {
                    propertyValue = propertyValue.toString();
                }
            } else if (beanContext != null && beanContext.isJsonDirect()) {
                //String jsonStr = (String) propertyValue;
                //propertyValue = Inspector.parse(jsonStr);
                throw new RuntimeException("unknow exception @ by ooknight");
            }
        }
        if (jsonBeanDeser.contextValueFilters != null) {
            for (ContextValueFilter valueFilter : jsonBeanDeser.contextValueFilters) {
                propertyValue = valueFilter.process(beanContext, object, key, propertyValue);
            }
        }
        if (this.contextValueFilters != null) {
            for (ContextValueFilter valueFilter : this.contextValueFilters) {
                propertyValue = valueFilter.process(beanContext, object, key, propertyValue);
            }
        }
        return propertyValue;
    }

    /**
     * only invoke by asm byte
     */
    protected boolean writeDirect(JSONSerializer jsonBeanDeser) {
        return jsonBeanDeser.out.writeDirect && this.writeDirect && jsonBeanDeser.writeDirect;
    }
}
