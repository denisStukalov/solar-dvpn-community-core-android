// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: sentinel/types/v1/status.proto

package sentinel.types.v1;

public final class StatusOuterClass {
  private StatusOuterClass() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  /**
   * Protobuf enum {@code sentinel.types.v1.Status}
   */
  public enum Status
      implements com.google.protobuf.ProtocolMessageEnum {
    /**
     * <code>STATUS_UNSPECIFIED = 0 [(.gogoproto.enumvalue_customname) = "Unspecified"];</code>
     */
    STATUS_UNSPECIFIED(0),
    /**
     * <code>STATUS_ACTIVE = 1 [(.gogoproto.enumvalue_customname) = "Active"];</code>
     */
    STATUS_ACTIVE(1),
    /**
     * <code>STATUS_INACTIVE_PENDING = 2 [(.gogoproto.enumvalue_customname) = "InactivePending"];</code>
     */
    STATUS_INACTIVE_PENDING(2),
    /**
     * <code>STATUS_INACTIVE = 3 [(.gogoproto.enumvalue_customname) = "Inactive"];</code>
     */
    STATUS_INACTIVE(3),
    UNRECOGNIZED(-1),
    ;

    /**
     * <code>STATUS_UNSPECIFIED = 0 [(.gogoproto.enumvalue_customname) = "Unspecified"];</code>
     */
    public static final int STATUS_UNSPECIFIED_VALUE = 0;
    /**
     * <code>STATUS_ACTIVE = 1 [(.gogoproto.enumvalue_customname) = "Active"];</code>
     */
    public static final int STATUS_ACTIVE_VALUE = 1;
    /**
     * <code>STATUS_INACTIVE_PENDING = 2 [(.gogoproto.enumvalue_customname) = "InactivePending"];</code>
     */
    public static final int STATUS_INACTIVE_PENDING_VALUE = 2;
    /**
     * <code>STATUS_INACTIVE = 3 [(.gogoproto.enumvalue_customname) = "Inactive"];</code>
     */
    public static final int STATUS_INACTIVE_VALUE = 3;


    public final int getNumber() {
      if (this == UNRECOGNIZED) {
        throw new java.lang.IllegalArgumentException(
            "Can't get the number of an unknown enum value.");
      }
      return value;
    }

    /**
     * @deprecated Use {@link #forNumber(int)} instead.
     */
    @java.lang.Deprecated
    public static Status valueOf(int value) {
      return forNumber(value);
    }

    public static Status forNumber(int value) {
      switch (value) {
        case 0: return STATUS_UNSPECIFIED;
        case 1: return STATUS_ACTIVE;
        case 2: return STATUS_INACTIVE_PENDING;
        case 3: return STATUS_INACTIVE;
        default: return null;
      }
    }

    public static com.google.protobuf.Internal.EnumLiteMap<Status>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static final com.google.protobuf.Internal.EnumLiteMap<
        Status> internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<Status>() {
            public Status findValueByNumber(int number) {
              return Status.forNumber(number);
            }
          };

    public final com.google.protobuf.Descriptors.EnumValueDescriptor
        getValueDescriptor() {
      return getDescriptor().getValues().get(ordinal());
    }
    public final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptorForType() {
      return getDescriptor();
    }
    public static final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptor() {
      return sentinel.types.v1.StatusOuterClass.getDescriptor().getEnumTypes().get(0);
    }

    private static final Status[] VALUES = values();

    public static Status valueOf(
        com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
      if (desc.getType() != getDescriptor()) {
        throw new java.lang.IllegalArgumentException(
          "EnumValueDescriptor is not for this type.");
      }
      if (desc.getIndex() == -1) {
        return UNRECOGNIZED;
      }
      return VALUES[desc.getIndex()];
    }

    private final int value;

    private Status(int value) {
      this.value = value;
    }

    // @@protoc_insertion_point(enum_scope:sentinel.types.v1.Status)
  }


  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\036sentinel/types/v1/status.proto\022\021sentin" +
      "el.types.v1\032\024gogoproto/gogo.proto*\245\001\n\006St" +
      "atus\022\'\n\022STATUS_UNSPECIFIED\020\000\032\017\212\235 \013Unspec" +
      "ified\022\035\n\rSTATUS_ACTIVE\020\001\032\n\212\235 \006Active\0220\n\027" +
      "STATUS_INACTIVE_PENDING\020\002\032\023\212\235 \017InactiveP" +
      "ending\022!\n\017STATUS_INACTIVE\020\003\032\014\212\235 \010Inactiv" +
      "eB0Z&github.com/sentinel-official/hub/ty" +
      "pes\310\341\036\000\320\341\036\000b\006proto3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          com.google.protobuf2.GoGoProtos.getDescriptor(),
        }, assigner);
    com.google.protobuf.ExtensionRegistry registry =
        com.google.protobuf.ExtensionRegistry.newInstance();
    registry.add(com.google.protobuf2.GoGoProtos.enumvalueCustomname);
    registry.add(com.google.protobuf2.GoGoProtos.goprotoEnumPrefixAll);
    registry.add(com.google.protobuf2.GoGoProtos.goprotoGettersAll);
    com.google.protobuf.Descriptors.FileDescriptor
        .internalUpdateFileDescriptor(descriptor, registry);
    com.google.protobuf2.GoGoProtos.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}