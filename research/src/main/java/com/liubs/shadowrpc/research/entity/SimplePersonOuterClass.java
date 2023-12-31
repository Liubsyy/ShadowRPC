package com.liubs.shadowrpc.research.entity;
// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: entity/SimplePerson.proto

// Protobuf Java Version: 3.25.1
public final class SimplePersonOuterClass {
  private SimplePersonOuterClass() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface SimplePersonOrBuilder extends
      // @@protoc_insertion_point(interface_extends:SimplePerson)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>int32 age = 1;</code>
     * @return The age.
     */
    int getAge();

    /**
     * <code>double height = 2;</code>
     * @return The height.
     */
    double getHeight();

    /**
     * <code>float weight = 3;</code>
     * @return The weight.
     */
    float getWeight();

    /**
     * <code>int64 money = 4;</code>
     * @return The money.
     */
    long getMoney();

    /**
     * <code>string name = 5;</code>
     * @return The name.
     */
    String getName();
    /**
     * <code>string name = 5;</code>
     * @return The bytes for name.
     */
    com.google.protobuf.ByteString
        getNameBytes();
  }
  /**
   * <pre>
   * ./protoc --java_out=./entity ./entity/Person.proto
   * </pre>
   *
   * Protobuf type {@code SimplePerson}
   */
  public static final class SimplePerson extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:SimplePerson)
      SimplePersonOrBuilder {
  private static final long serialVersionUID = 0L;
    // Use SimplePerson.newBuilder() to construct.
    private SimplePerson(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private SimplePerson() {
      name_ = "";
    }

    @Override
    @SuppressWarnings({"unused"})
    protected Object newInstance(
        UnusedPrivateParameter unused) {
      return new SimplePerson();
    }

    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return SimplePersonOuterClass.internal_static_SimplePerson_descriptor;
    }

    @Override
    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return SimplePersonOuterClass.internal_static_SimplePerson_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              SimplePerson.class, Builder.class);
    }

    public static final int AGE_FIELD_NUMBER = 1;
    private int age_ = 0;
    /**
     * <code>int32 age = 1;</code>
     * @return The age.
     */
    @Override
    public int getAge() {
      return age_;
    }

    public static final int HEIGHT_FIELD_NUMBER = 2;
    private double height_ = 0D;
    /**
     * <code>double height = 2;</code>
     * @return The height.
     */
    @Override
    public double getHeight() {
      return height_;
    }

    public static final int WEIGHT_FIELD_NUMBER = 3;
    private float weight_ = 0F;
    /**
     * <code>float weight = 3;</code>
     * @return The weight.
     */
    @Override
    public float getWeight() {
      return weight_;
    }

    public static final int MONEY_FIELD_NUMBER = 4;
    private long money_ = 0L;
    /**
     * <code>int64 money = 4;</code>
     * @return The money.
     */
    @Override
    public long getMoney() {
      return money_;
    }

    public static final int NAME_FIELD_NUMBER = 5;
    @SuppressWarnings("serial")
    private volatile Object name_ = "";
    /**
     * <code>string name = 5;</code>
     * @return The name.
     */
    @Override
    public String getName() {
      Object ref = name_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        name_ = s;
        return s;
      }
    }
    /**
     * <code>string name = 5;</code>
     * @return The bytes for name.
     */
    @Override
    public com.google.protobuf.ByteString
        getNameBytes() {
      Object ref = name_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        name_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    private byte memoizedIsInitialized = -1;
    @Override
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    @Override
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      if (age_ != 0) {
        output.writeInt32(1, age_);
      }
      if (Double.doubleToRawLongBits(height_) != 0) {
        output.writeDouble(2, height_);
      }
      if (Float.floatToRawIntBits(weight_) != 0) {
        output.writeFloat(3, weight_);
      }
      if (money_ != 0L) {
        output.writeInt64(4, money_);
      }
      if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(name_)) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 5, name_);
      }
      getUnknownFields().writeTo(output);
    }

    @Override
    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (age_ != 0) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(1, age_);
      }
      if (Double.doubleToRawLongBits(height_) != 0) {
        size += com.google.protobuf.CodedOutputStream
          .computeDoubleSize(2, height_);
      }
      if (Float.floatToRawIntBits(weight_) != 0) {
        size += com.google.protobuf.CodedOutputStream
          .computeFloatSize(3, weight_);
      }
      if (money_ != 0L) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt64Size(4, money_);
      }
      if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(name_)) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(5, name_);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSize = size;
      return size;
    }

    @Override
    public boolean equals(final Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof SimplePerson)) {
        return super.equals(obj);
      }
      SimplePerson other = (SimplePerson) obj;

      if (getAge()
          != other.getAge()) return false;
      if (Double.doubleToLongBits(getHeight())
          != Double.doubleToLongBits(
              other.getHeight())) return false;
      if (Float.floatToIntBits(getWeight())
          != Float.floatToIntBits(
              other.getWeight())) return false;
      if (getMoney()
          != other.getMoney()) return false;
      if (!getName()
          .equals(other.getName())) return false;
      if (!getUnknownFields().equals(other.getUnknownFields())) return false;
      return true;
    }

    @Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptor().hashCode();
      hash = (37 * hash) + AGE_FIELD_NUMBER;
      hash = (53 * hash) + getAge();
      hash = (37 * hash) + HEIGHT_FIELD_NUMBER;
      hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
          Double.doubleToLongBits(getHeight()));
      hash = (37 * hash) + WEIGHT_FIELD_NUMBER;
      hash = (53 * hash) + Float.floatToIntBits(
          getWeight());
      hash = (37 * hash) + MONEY_FIELD_NUMBER;
      hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
          getMoney());
      hash = (37 * hash) + NAME_FIELD_NUMBER;
      hash = (53 * hash) + getName().hashCode();
      hash = (29 * hash) + getUnknownFields().hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static SimplePerson parseFrom(
        java.nio.ByteBuffer data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static SimplePerson parseFrom(
        java.nio.ByteBuffer data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static SimplePerson parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static SimplePerson parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static SimplePerson parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static SimplePerson parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static SimplePerson parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static SimplePerson parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    public static SimplePerson parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }

    public static SimplePerson parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static SimplePerson parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static SimplePerson parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    @Override
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(SimplePerson prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    @Override
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
          ? new Builder() : new Builder().mergeFrom(this);
    }

    @Override
    protected Builder newBuilderForType(
        BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * <pre>
     * ./protoc --java_out=./entity ./entity/Person.proto
     * </pre>
     *
     * Protobuf type {@code SimplePerson}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:SimplePerson)
        SimplePersonOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return SimplePersonOuterClass.internal_static_SimplePerson_descriptor;
      }

      @Override
      protected FieldAccessorTable
          internalGetFieldAccessorTable() {
        return SimplePersonOuterClass.internal_static_SimplePerson_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                SimplePerson.class, Builder.class);
      }

      // Construct using SimplePersonOuterClass.SimplePerson.newBuilder()
      private Builder() {

      }

      private Builder(
          BuilderParent parent) {
        super(parent);

      }
      @Override
      public Builder clear() {
        super.clear();
        bitField0_ = 0;
        age_ = 0;
        height_ = 0D;
        weight_ = 0F;
        money_ = 0L;
        name_ = "";
        return this;
      }

      @Override
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return SimplePersonOuterClass.internal_static_SimplePerson_descriptor;
      }

      @Override
      public SimplePerson getDefaultInstanceForType() {
        return SimplePerson.getDefaultInstance();
      }

      @Override
      public SimplePerson build() {
        SimplePerson result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      @Override
      public SimplePerson buildPartial() {
        SimplePerson result = new SimplePerson(this);
        if (bitField0_ != 0) { buildPartial0(result); }
        onBuilt();
        return result;
      }

      private void buildPartial0(SimplePerson result) {
        int from_bitField0_ = bitField0_;
        if (((from_bitField0_ & 0x00000001) != 0)) {
          result.age_ = age_;
        }
        if (((from_bitField0_ & 0x00000002) != 0)) {
          result.height_ = height_;
        }
        if (((from_bitField0_ & 0x00000004) != 0)) {
          result.weight_ = weight_;
        }
        if (((from_bitField0_ & 0x00000008) != 0)) {
          result.money_ = money_;
        }
        if (((from_bitField0_ & 0x00000010) != 0)) {
          result.name_ = name_;
        }
      }

      @Override
      public Builder clone() {
        return super.clone();
      }
      @Override
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return super.setField(field, value);
      }
      @Override
      public Builder clearField(
          com.google.protobuf.Descriptors.FieldDescriptor field) {
        return super.clearField(field);
      }
      @Override
      public Builder clearOneof(
          com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return super.clearOneof(oneof);
      }
      @Override
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index, Object value) {
        return super.setRepeatedField(field, index, value);
      }
      @Override
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return super.addRepeatedField(field, value);
      }
      @Override
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof SimplePerson) {
          return mergeFrom((SimplePerson)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(SimplePerson other) {
        if (other == SimplePerson.getDefaultInstance()) return this;
        if (other.getAge() != 0) {
          setAge(other.getAge());
        }
        if (other.getHeight() != 0D) {
          setHeight(other.getHeight());
        }
        if (other.getWeight() != 0F) {
          setWeight(other.getWeight());
        }
        if (other.getMoney() != 0L) {
          setMoney(other.getMoney());
        }
        if (!other.getName().isEmpty()) {
          name_ = other.name_;
          bitField0_ |= 0x00000010;
          onChanged();
        }
        this.mergeUnknownFields(other.getUnknownFields());
        onChanged();
        return this;
      }

      @Override
      public final boolean isInitialized() {
        return true;
      }

      @Override
      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        if (extensionRegistry == null) {
          throw new NullPointerException();
        }
        try {
          boolean done = false;
          while (!done) {
            int tag = input.readTag();
            switch (tag) {
              case 0:
                done = true;
                break;
              case 8: {
                age_ = input.readInt32();
                bitField0_ |= 0x00000001;
                break;
              } // case 8
              case 17: {
                height_ = input.readDouble();
                bitField0_ |= 0x00000002;
                break;
              } // case 17
              case 29: {
                weight_ = input.readFloat();
                bitField0_ |= 0x00000004;
                break;
              } // case 29
              case 32: {
                money_ = input.readInt64();
                bitField0_ |= 0x00000008;
                break;
              } // case 32
              case 42: {
                name_ = input.readStringRequireUtf8();
                bitField0_ |= 0x00000010;
                break;
              } // case 42
              default: {
                if (!super.parseUnknownField(input, extensionRegistry, tag)) {
                  done = true; // was an endgroup tag
                }
                break;
              } // default:
            } // switch (tag)
          } // while (!done)
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          throw e.unwrapIOException();
        } finally {
          onChanged();
        } // finally
        return this;
      }
      private int bitField0_;

      private int age_ ;
      /**
       * <code>int32 age = 1;</code>
       * @return The age.
       */
      @Override
      public int getAge() {
        return age_;
      }
      /**
       * <code>int32 age = 1;</code>
       * @param value The age to set.
       * @return This builder for chaining.
       */
      public Builder setAge(int value) {

        age_ = value;
        bitField0_ |= 0x00000001;
        onChanged();
        return this;
      }
      /**
       * <code>int32 age = 1;</code>
       * @return This builder for chaining.
       */
      public Builder clearAge() {
        bitField0_ = (bitField0_ & ~0x00000001);
        age_ = 0;
        onChanged();
        return this;
      }

      private double height_ ;
      /**
       * <code>double height = 2;</code>
       * @return The height.
       */
      @Override
      public double getHeight() {
        return height_;
      }
      /**
       * <code>double height = 2;</code>
       * @param value The height to set.
       * @return This builder for chaining.
       */
      public Builder setHeight(double value) {

        height_ = value;
        bitField0_ |= 0x00000002;
        onChanged();
        return this;
      }
      /**
       * <code>double height = 2;</code>
       * @return This builder for chaining.
       */
      public Builder clearHeight() {
        bitField0_ = (bitField0_ & ~0x00000002);
        height_ = 0D;
        onChanged();
        return this;
      }

      private float weight_ ;
      /**
       * <code>float weight = 3;</code>
       * @return The weight.
       */
      @Override
      public float getWeight() {
        return weight_;
      }
      /**
       * <code>float weight = 3;</code>
       * @param value The weight to set.
       * @return This builder for chaining.
       */
      public Builder setWeight(float value) {

        weight_ = value;
        bitField0_ |= 0x00000004;
        onChanged();
        return this;
      }
      /**
       * <code>float weight = 3;</code>
       * @return This builder for chaining.
       */
      public Builder clearWeight() {
        bitField0_ = (bitField0_ & ~0x00000004);
        weight_ = 0F;
        onChanged();
        return this;
      }

      private long money_ ;
      /**
       * <code>int64 money = 4;</code>
       * @return The money.
       */
      @Override
      public long getMoney() {
        return money_;
      }
      /**
       * <code>int64 money = 4;</code>
       * @param value The money to set.
       * @return This builder for chaining.
       */
      public Builder setMoney(long value) {

        money_ = value;
        bitField0_ |= 0x00000008;
        onChanged();
        return this;
      }
      /**
       * <code>int64 money = 4;</code>
       * @return This builder for chaining.
       */
      public Builder clearMoney() {
        bitField0_ = (bitField0_ & ~0x00000008);
        money_ = 0L;
        onChanged();
        return this;
      }

      private Object name_ = "";
      /**
       * <code>string name = 5;</code>
       * @return The name.
       */
      public String getName() {
        Object ref = name_;
        if (!(ref instanceof String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          String s = bs.toStringUtf8();
          name_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      /**
       * <code>string name = 5;</code>
       * @return The bytes for name.
       */
      public com.google.protobuf.ByteString
          getNameBytes() {
        Object ref = name_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (String) ref);
          name_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>string name = 5;</code>
       * @param value The name to set.
       * @return This builder for chaining.
       */
      public Builder setName(
          String value) {
        if (value == null) { throw new NullPointerException(); }
        name_ = value;
        bitField0_ |= 0x00000010;
        onChanged();
        return this;
      }
      /**
       * <code>string name = 5;</code>
       * @return This builder for chaining.
       */
      public Builder clearName() {
        name_ = getDefaultInstance().getName();
        bitField0_ = (bitField0_ & ~0x00000010);
        onChanged();
        return this;
      }
      /**
       * <code>string name = 5;</code>
       * @param value The bytes for name to set.
       * @return This builder for chaining.
       */
      public Builder setNameBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) { throw new NullPointerException(); }
        checkByteStringIsUtf8(value);
        name_ = value;
        bitField0_ |= 0x00000010;
        onChanged();
        return this;
      }
      @Override
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.setUnknownFields(unknownFields);
      }

      @Override
      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.mergeUnknownFields(unknownFields);
      }


      // @@protoc_insertion_point(builder_scope:SimplePerson)
    }

    // @@protoc_insertion_point(class_scope:SimplePerson)
    private static final SimplePerson DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new SimplePerson();
    }

    public static SimplePerson getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<SimplePerson>
        PARSER = new com.google.protobuf.AbstractParser<SimplePerson>() {
      @Override
      public SimplePerson parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        Builder builder = newBuilder();
        try {
          builder.mergeFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          throw e.setUnfinishedMessage(builder.buildPartial());
        } catch (com.google.protobuf.UninitializedMessageException e) {
          throw e.asInvalidProtocolBufferException().setUnfinishedMessage(builder.buildPartial());
        } catch (java.io.IOException e) {
          throw new com.google.protobuf.InvalidProtocolBufferException(e)
              .setUnfinishedMessage(builder.buildPartial());
        }
        return builder.buildPartial();
      }
    };

    public static com.google.protobuf.Parser<SimplePerson> parser() {
      return PARSER;
    }

    @Override
    public com.google.protobuf.Parser<SimplePerson> getParserForType() {
      return PARSER;
    }

    @Override
    public SimplePerson getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_SimplePerson_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_SimplePerson_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n\031entity/SimplePerson.proto\"X\n\014SimplePer" +
      "son\022\013\n\003age\030\001 \001(\005\022\016\n\006height\030\002 \001(\001\022\016\n\006weig" +
      "ht\030\003 \001(\002\022\r\n\005money\030\004 \001(\003\022\014\n\004name\030\005 \001(\tb\006p" +
      "roto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        });
    internal_static_SimplePerson_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_SimplePerson_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_SimplePerson_descriptor,
        new String[] { "Age", "Height", "Weight", "Money", "Name", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}