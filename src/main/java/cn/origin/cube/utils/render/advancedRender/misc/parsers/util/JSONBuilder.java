// 
// Decompiled by Procyon v0.5.36
// 

package cn.origin.cube.utils.render.advancedRender.misc.parsers.util;

public final class JSONBuilder
{
    private String value;
    
    public JSONBuilder() {
        this.value = "";
        this.value += "{";
    }
    
    public JSONBuilder value(final String key, final String value) {
        this.value = this.value + "\"" + key + "\": \"" + value + "\",";
        return this;
    }
    
    public String build() {
        if (this.value.endsWith(",")) {
            this.value = this.value.substring(0, this.value.length() - 1);
        }
        if (!this.value.endsWith("}")) {
            this.value += "}";
        }
        return this.value;
    }
}
