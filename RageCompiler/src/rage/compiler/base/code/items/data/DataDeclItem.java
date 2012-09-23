package rage.compiler.base.code.items.data;

import rage.compiler.base.code.items.GenericCodeItem;

/**
 * Data declaration
 */

public class DataDeclItem extends GenericCodeItem {
    protected DataItem data;
    protected DataTypeItem type;

    public DataDeclItem(DataTypeItem type, DataItem data) {
        this.data = data;
        this.type = type;
    }

    public final DataItem getData()  {
        return data;
    }

    public final DataTypeItem getDataType()  {
        return type;
    }

    @Override
    public Type getType() {
        return Type.DATA_DECL;
    }
}
