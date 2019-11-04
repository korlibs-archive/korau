package com.soywiz.korau.format.mp3


//ENTRY Program
//Program.main(arrayOf())
@Suppress("MemberVisibilityCanBePrivate", "FunctionName", "CanBeVal", "DoubleNegation", "LocalVariableName", "NAME_SHADOWING", "VARIABLE_WITH_REDUNDANT_INITIALIZER", "RemoveRedundantCallsOfConversionMethods", "EXPERIMENTAL_IS_NOT_ENABLED", "RedundantExplicitType", "RemoveExplicitTypeArguments", "RedundantExplicitType", "unused", "UNCHECKED_CAST", "UNUSED_VARIABLE", "UNUSED_PARAMETER", "NOTHING_TO_INLINE", "PropertyName", "ClassName", "USELESS_CAST", "PrivatePropertyName", "CanBeParameter", "UnusedMainParameter")
@UseExperimental(ExperimentalUnsignedTypes::class)
class MiniMp3(HEAP_SIZE: Int = 0) : Runtime(HEAP_SIZE) {
    companion object {
        const val MINIMP3_IMPLEMENTATION = 1
        const val MINIMP3_NO_SIMD = 1
        const val MINIMP3_MAX_SAMPLES_PER_FRAME = 2304
        const val MAX_FREE_FORMAT_FRAME_SIZE = 2304
        const val MAX_FRAME_SYNC_MATCHES = 10
        const val MAX_L3_FRAME_PAYLOAD_BYTES = 2304
        const val MAX_BITRESERVOIR_BYTES = 511
        const val SHORT_BLOCK_TYPE = 2
        const val STOP_BLOCK_TYPE = 3
        const val MODE_MONO = 3
        const val MODE_JOINT_STEREO = 1
        const val HDR_SIZE = 4
        const val HAVE_SIMD = 0
    }

    fun __STATIC_hdr_bitrate_kbps_halfrate__INIT() = Array2Array3Array15UByteAlloc(Array3Array15UByteAlloc(Array15UByteAlloc((0.toUByte()), (4.toUByte()), (8.toUByte()), (12.toUByte()), (16.toUByte()), (20.toUByte()), (24.toUByte()), (28.toUByte()), (32.toUByte()), (40.toUByte()), (48.toUByte()), (56.toUByte()), (64.toUByte()), (72.toUByte()), (80.toUByte())), Array15UByteAlloc((0.toUByte()), (4.toUByte()), (8.toUByte()), (12.toUByte()), (16.toUByte()), (20.toUByte()), (24.toUByte()), (28.toUByte()), (32.toUByte()), (40.toUByte()), (48.toUByte()), (56.toUByte()), (64.toUByte()), (72.toUByte()), (80.toUByte())), Array15UByteAlloc((0.toUByte()), (16.toUByte()), (24.toUByte()), (28.toUByte()), (32.toUByte()), (40.toUByte()), (48.toUByte()), (56.toUByte()), (64.toUByte()), (72.toUByte()), (80.toUByte()), (88.toUByte()), (96.toUByte()), (112.toUByte()), (128.toUByte()))), Array3Array15UByteAlloc(Array15UByteAlloc((0.toUByte()), (16.toUByte()), (20.toUByte()), (24.toUByte()), (28.toUByte()), (32.toUByte()), (40.toUByte()), (48.toUByte()), (56.toUByte()), (64.toUByte()), (80.toUByte()), (96.toUByte()), (112.toUByte()), (128.toUByte()), (160.toUByte())), Array15UByteAlloc((0.toUByte()), (16.toUByte()), (24.toUByte()), (28.toUByte()), (32.toUByte()), (40.toUByte()), (48.toUByte()), (56.toUByte()), (64.toUByte()), (80.toUByte()), (96.toUByte()), (112.toUByte()), (128.toUByte()), (160.toUByte()), (192.toUByte())), Array15UByteAlloc((0.toUByte()), (16.toUByte()), (32.toUByte()), (48.toUByte()), (64.toUByte()), (80.toUByte()), (96.toUByte()), (112.toUByte()), (128.toUByte()), (144.toUByte()), (160.toUByte()), (176.toUByte()), (192.toUByte()), (208.toUByte()), (224.toUByte()))))
    var __STATIC_hdr_bitrate_kbps_halfrate: Array2Array3Array15UByte = __STATIC_hdr_bitrate_kbps_halfrate__INIT()
    fun __STATIC_hdr_sample_rate_hz_g_hz__INIT() = Array3UIntAlloc((44100.toUInt()), (48000.toUInt()), (32000.toUInt()))
    var __STATIC_hdr_sample_rate_hz_g_hz: Array3UInt = __STATIC_hdr_sample_rate_hz_g_hz__INIT()
    fun __STATIC_L12_subband_alloc_table_g_alloc_L1__INIT() = fixedArrayOfL12_subband_alloc_t(1, L12_subband_alloc_tAlloc(tab_offset = (76.toUByte()), code_tab_width = (4.toUByte()), band_count = (32.toUByte())))
    var __STATIC_L12_subband_alloc_table_g_alloc_L1: CPointer<L12_subband_alloc_t> = __STATIC_L12_subband_alloc_table_g_alloc_L1__INIT()
    fun __STATIC_L12_subband_alloc_table_g_alloc_L2M2__INIT() = fixedArrayOfL12_subband_alloc_t(3, L12_subband_alloc_tAlloc(tab_offset = (60.toUByte()), code_tab_width = (4.toUByte()), band_count = (4.toUByte())), L12_subband_alloc_tAlloc(tab_offset = (44.toUByte()), code_tab_width = (3.toUByte()), band_count = (7.toUByte())), L12_subband_alloc_tAlloc(tab_offset = (44.toUByte()), code_tab_width = (2.toUByte()), band_count = (19.toUByte())))
    var __STATIC_L12_subband_alloc_table_g_alloc_L2M2: CPointer<L12_subband_alloc_t> = __STATIC_L12_subband_alloc_table_g_alloc_L2M2__INIT()
    fun __STATIC_L12_subband_alloc_table_g_alloc_L2M1__INIT() = fixedArrayOfL12_subband_alloc_t(4, L12_subband_alloc_tAlloc(tab_offset = (0.toUByte()), code_tab_width = (4.toUByte()), band_count = (3.toUByte())), L12_subband_alloc_tAlloc(tab_offset = (16.toUByte()), code_tab_width = (4.toUByte()), band_count = (8.toUByte())), L12_subband_alloc_tAlloc(tab_offset = (32.toUByte()), code_tab_width = (3.toUByte()), band_count = (12.toUByte())), L12_subband_alloc_tAlloc(tab_offset = (40.toUByte()), code_tab_width = (2.toUByte()), band_count = (7.toUByte())))
    var __STATIC_L12_subband_alloc_table_g_alloc_L2M1: CPointer<L12_subband_alloc_t> = __STATIC_L12_subband_alloc_table_g_alloc_L2M1__INIT()
    fun __STATIC_L12_subband_alloc_table_g_alloc_L2M1_lowrate__INIT() = fixedArrayOfL12_subband_alloc_t(2, L12_subband_alloc_tAlloc(tab_offset = (44.toUByte()), code_tab_width = (4.toUByte()), band_count = (2.toUByte())), L12_subband_alloc_tAlloc(tab_offset = (44.toUByte()), code_tab_width = (3.toUByte()), band_count = (10.toUByte())))
    var __STATIC_L12_subband_alloc_table_g_alloc_L2M1_lowrate: CPointer<L12_subband_alloc_t> = __STATIC_L12_subband_alloc_table_g_alloc_L2M1_lowrate__INIT()
    fun __STATIC_L12_read_scalefactors_g_deq_L12__INIT() = Array54FloatAlloc((9.53674316e-7f / (3.toFloat())), (7.56931807e-7f / (3.toFloat())), (6.00777173e-7f / (3.toFloat())), (9.53674316e-7f / (7.toFloat())), (7.56931807e-7f / (7.toFloat())), (6.00777173e-7f / (7.toFloat())), (9.53674316e-7f / (15.toFloat())), (7.56931807e-7f / (15.toFloat())), (6.00777173e-7f / (15.toFloat())), (9.53674316e-7f / (31.toFloat())), (7.56931807e-7f / (31.toFloat())), (6.00777173e-7f / (31.toFloat())), (9.53674316e-7f / (63.toFloat())), (7.56931807e-7f / (63.toFloat())), (6.00777173e-7f / (63.toFloat())), (9.53674316e-7f / (127.toFloat())), (7.56931807e-7f / (127.toFloat())), (6.00777173e-7f / (127.toFloat())), (9.53674316e-7f / (255.toFloat())), (7.56931807e-7f / (255.toFloat())), (6.00777173e-7f / (255.toFloat())), (9.53674316e-7f / (511.toFloat())), (7.56931807e-7f / (511.toFloat())), (6.00777173e-7f / (511.toFloat())), (9.53674316e-7f / (1023.toFloat())), (7.56931807e-7f / (1023.toFloat())), (6.00777173e-7f / (1023.toFloat())), (9.53674316e-7f / (2047.toFloat())), (7.56931807e-7f / (2047.toFloat())), (6.00777173e-7f / (2047.toFloat())), (9.53674316e-7f / (4095.toFloat())), (7.56931807e-7f / (4095.toFloat())), (6.00777173e-7f / (4095.toFloat())), (9.53674316e-7f / (8191.toFloat())), (7.56931807e-7f / (8191.toFloat())), (6.00777173e-7f / (8191.toFloat())), (9.53674316e-7f / (16383.toFloat())), (7.56931807e-7f / (16383.toFloat())), (6.00777173e-7f / (16383.toFloat())), (9.53674316e-7f / (32767.toFloat())), (7.56931807e-7f / (32767.toFloat())), (6.00777173e-7f / (32767.toFloat())), (9.53674316e-7f / (65535.toFloat())), (7.56931807e-7f / (65535.toFloat())), (6.00777173e-7f / (65535.toFloat())), (9.53674316e-7f / (3.toFloat())), (7.56931807e-7f / (3.toFloat())), (6.00777173e-7f / (3.toFloat())), (9.53674316e-7f / (5.toFloat())), (7.56931807e-7f / (5.toFloat())), (6.00777173e-7f / (5.toFloat())), (9.53674316e-7f / (9.toFloat())), (7.56931807e-7f / (9.toFloat())), (6.00777173e-7f / (9.toFloat())))
    var __STATIC_L12_read_scalefactors_g_deq_L12: Array54Float = __STATIC_L12_read_scalefactors_g_deq_L12__INIT()
    fun __STATIC_L12_read_scale_info_g_bitalloc_code_tab__INIT() = fixedArrayOfUByte(92, (0.toUByte()), (17.toUByte()), (3.toUByte()), (4.toUByte()), (5.toUByte()), (6.toUByte()), (7.toUByte()), (8.toUByte()), (9.toUByte()), (10.toUByte()), (11.toUByte()), (12.toUByte()), (13.toUByte()), (14.toUByte()), (15.toUByte()), (16.toUByte()), (0.toUByte()), (17.toUByte()), (18.toUByte()), (3.toUByte()), (19.toUByte()), (4.toUByte()), (5.toUByte()), (6.toUByte()), (7.toUByte()), (8.toUByte()), (9.toUByte()), (10.toUByte()), (11.toUByte()), (12.toUByte()), (13.toUByte()), (16.toUByte()), (0.toUByte()), (17.toUByte()), (18.toUByte()), (3.toUByte()), (19.toUByte()), (4.toUByte()), (5.toUByte()), (16.toUByte()), (0.toUByte()), (17.toUByte()), (18.toUByte()), (16.toUByte()), (0.toUByte()), (17.toUByte()), (18.toUByte()), (19.toUByte()), (4.toUByte()), (5.toUByte()), (6.toUByte()), (7.toUByte()), (8.toUByte()), (9.toUByte()), (10.toUByte()), (11.toUByte()), (12.toUByte()), (13.toUByte()), (14.toUByte()), (15.toUByte()), (0.toUByte()), (17.toUByte()), (18.toUByte()), (3.toUByte()), (19.toUByte()), (4.toUByte()), (5.toUByte()), (6.toUByte()), (7.toUByte()), (8.toUByte()), (9.toUByte()), (10.toUByte()), (11.toUByte()), (12.toUByte()), (13.toUByte()), (14.toUByte()), (0.toUByte()), (2.toUByte()), (3.toUByte()), (4.toUByte()), (5.toUByte()), (6.toUByte()), (7.toUByte()), (8.toUByte()), (9.toUByte()), (10.toUByte()), (11.toUByte()), (12.toUByte()), (13.toUByte()), (14.toUByte()), (15.toUByte()), (16.toUByte()))
    var __STATIC_L12_read_scale_info_g_bitalloc_code_tab: CPointer<UByte> = __STATIC_L12_read_scale_info_g_bitalloc_code_tab__INIT()
    fun __STATIC_L3_read_side_info_g_scf_long__INIT() = Array8Array23UByteAlloc(Array23UByteAlloc((6.toUByte()), (6.toUByte()), (6.toUByte()), (6.toUByte()), (6.toUByte()), (6.toUByte()), (8.toUByte()), (10.toUByte()), (12.toUByte()), (14.toUByte()), (16.toUByte()), (20.toUByte()), (24.toUByte()), (28.toUByte()), (32.toUByte()), (38.toUByte()), (46.toUByte()), (52.toUByte()), (60.toUByte()), (68.toUByte()), (58.toUByte()), (54.toUByte()), (0.toUByte())), Array23UByteAlloc((12.toUByte()), (12.toUByte()), (12.toUByte()), (12.toUByte()), (12.toUByte()), (12.toUByte()), (16.toUByte()), (20.toUByte()), (24.toUByte()), (28.toUByte()), (32.toUByte()), (40.toUByte()), (48.toUByte()), (56.toUByte()), (64.toUByte()), (76.toUByte()), (90.toUByte()), (2.toUByte()), (2.toUByte()), (2.toUByte()), (2.toUByte()), (2.toUByte()), (0.toUByte())), Array23UByteAlloc((6.toUByte()), (6.toUByte()), (6.toUByte()), (6.toUByte()), (6.toUByte()), (6.toUByte()), (8.toUByte()), (10.toUByte()), (12.toUByte()), (14.toUByte()), (16.toUByte()), (20.toUByte()), (24.toUByte()), (28.toUByte()), (32.toUByte()), (38.toUByte()), (46.toUByte()), (52.toUByte()), (60.toUByte()), (68.toUByte()), (58.toUByte()), (54.toUByte()), (0.toUByte())), Array23UByteAlloc((6.toUByte()), (6.toUByte()), (6.toUByte()), (6.toUByte()), (6.toUByte()), (6.toUByte()), (8.toUByte()), (10.toUByte()), (12.toUByte()), (14.toUByte()), (16.toUByte()), (18.toUByte()), (22.toUByte()), (26.toUByte()), (32.toUByte()), (38.toUByte()), (46.toUByte()), (54.toUByte()), (62.toUByte()), (70.toUByte()), (76.toUByte()), (36.toUByte()), (0.toUByte())), Array23UByteAlloc((6.toUByte()), (6.toUByte()), (6.toUByte()), (6.toUByte()), (6.toUByte()), (6.toUByte()), (8.toUByte()), (10.toUByte()), (12.toUByte()), (14.toUByte()), (16.toUByte()), (20.toUByte()), (24.toUByte()), (28.toUByte()), (32.toUByte()), (38.toUByte()), (46.toUByte()), (52.toUByte()), (60.toUByte()), (68.toUByte()), (58.toUByte()), (54.toUByte()), (0.toUByte())), Array23UByteAlloc((4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (6.toUByte()), (6.toUByte()), (8.toUByte()), (8.toUByte()), (10.toUByte()), (12.toUByte()), (16.toUByte()), (20.toUByte()), (24.toUByte()), (28.toUByte()), (34.toUByte()), (42.toUByte()), (50.toUByte()), (54.toUByte()), (76.toUByte()), (158.toUByte()), (0.toUByte())), Array23UByteAlloc((4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (6.toUByte()), (6.toUByte()), (6.toUByte()), (8.toUByte()), (10.toUByte()), (12.toUByte()), (16.toUByte()), (18.toUByte()), (22.toUByte()), (28.toUByte()), (34.toUByte()), (40.toUByte()), (46.toUByte()), (54.toUByte()), (54.toUByte()), (192.toUByte()), (0.toUByte())), Array23UByteAlloc((4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (6.toUByte()), (6.toUByte()), (8.toUByte()), (10.toUByte()), (12.toUByte()), (16.toUByte()), (20.toUByte()), (24.toUByte()), (30.toUByte()), (38.toUByte()), (46.toUByte()), (56.toUByte()), (68.toUByte()), (84.toUByte()), (102.toUByte()), (26.toUByte()), (0.toUByte())))
    var __STATIC_L3_read_side_info_g_scf_long: Array8Array23UByte = __STATIC_L3_read_side_info_g_scf_long__INIT()
    fun __STATIC_L3_read_side_info_g_scf_short__INIT() = Array8Array40UByteAlloc(Array40UByteAlloc((4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (6.toUByte()), (6.toUByte()), (6.toUByte()), (8.toUByte()), (8.toUByte()), (8.toUByte()), (10.toUByte()), (10.toUByte()), (10.toUByte()), (12.toUByte()), (12.toUByte()), (12.toUByte()), (14.toUByte()), (14.toUByte()), (14.toUByte()), (18.toUByte()), (18.toUByte()), (18.toUByte()), (24.toUByte()), (24.toUByte()), (24.toUByte()), (30.toUByte()), (30.toUByte()), (30.toUByte()), (40.toUByte()), (40.toUByte()), (40.toUByte()), (18.toUByte()), (18.toUByte()), (18.toUByte()), (0.toUByte())), Array40UByteAlloc((8.toUByte()), (8.toUByte()), (8.toUByte()), (8.toUByte()), (8.toUByte()), (8.toUByte()), (8.toUByte()), (8.toUByte()), (8.toUByte()), (12.toUByte()), (12.toUByte()), (12.toUByte()), (16.toUByte()), (16.toUByte()), (16.toUByte()), (20.toUByte()), (20.toUByte()), (20.toUByte()), (24.toUByte()), (24.toUByte()), (24.toUByte()), (28.toUByte()), (28.toUByte()), (28.toUByte()), (36.toUByte()), (36.toUByte()), (36.toUByte()), (2.toUByte()), (2.toUByte()), (2.toUByte()), (2.toUByte()), (2.toUByte()), (2.toUByte()), (2.toUByte()), (2.toUByte()), (2.toUByte()), (26.toUByte()), (26.toUByte()), (26.toUByte()), (0.toUByte())), Array40UByteAlloc((4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (6.toUByte()), (6.toUByte()), (6.toUByte()), (6.toUByte()), (6.toUByte()), (6.toUByte()), (8.toUByte()), (8.toUByte()), (8.toUByte()), (10.toUByte()), (10.toUByte()), (10.toUByte()), (14.toUByte()), (14.toUByte()), (14.toUByte()), (18.toUByte()), (18.toUByte()), (18.toUByte()), (26.toUByte()), (26.toUByte()), (26.toUByte()), (32.toUByte()), (32.toUByte()), (32.toUByte()), (42.toUByte()), (42.toUByte()), (42.toUByte()), (18.toUByte()), (18.toUByte()), (18.toUByte()), (0.toUByte())), Array40UByteAlloc((4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (6.toUByte()), (6.toUByte()), (6.toUByte()), (8.toUByte()), (8.toUByte()), (8.toUByte()), (10.toUByte()), (10.toUByte()), (10.toUByte()), (12.toUByte()), (12.toUByte()), (12.toUByte()), (14.toUByte()), (14.toUByte()), (14.toUByte()), (18.toUByte()), (18.toUByte()), (18.toUByte()), (24.toUByte()), (24.toUByte()), (24.toUByte()), (32.toUByte()), (32.toUByte()), (32.toUByte()), (44.toUByte()), (44.toUByte()), (44.toUByte()), (12.toUByte()), (12.toUByte()), (12.toUByte()), (0.toUByte())), Array40UByteAlloc((4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (6.toUByte()), (6.toUByte()), (6.toUByte()), (8.toUByte()), (8.toUByte()), (8.toUByte()), (10.toUByte()), (10.toUByte()), (10.toUByte()), (12.toUByte()), (12.toUByte()), (12.toUByte()), (14.toUByte()), (14.toUByte()), (14.toUByte()), (18.toUByte()), (18.toUByte()), (18.toUByte()), (24.toUByte()), (24.toUByte()), (24.toUByte()), (30.toUByte()), (30.toUByte()), (30.toUByte()), (40.toUByte()), (40.toUByte()), (40.toUByte()), (18.toUByte()), (18.toUByte()), (18.toUByte()), (0.toUByte())), Array40UByteAlloc((4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (6.toUByte()), (6.toUByte()), (6.toUByte()), (8.toUByte()), (8.toUByte()), (8.toUByte()), (10.toUByte()), (10.toUByte()), (10.toUByte()), (12.toUByte()), (12.toUByte()), (12.toUByte()), (14.toUByte()), (14.toUByte()), (14.toUByte()), (18.toUByte()), (18.toUByte()), (18.toUByte()), (22.toUByte()), (22.toUByte()), (22.toUByte()), (30.toUByte()), (30.toUByte()), (30.toUByte()), (56.toUByte()), (56.toUByte()), (56.toUByte()), (0.toUByte())), Array40UByteAlloc((4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (6.toUByte()), (6.toUByte()), (6.toUByte()), (6.toUByte()), (6.toUByte()), (6.toUByte()), (10.toUByte()), (10.toUByte()), (10.toUByte()), (12.toUByte()), (12.toUByte()), (12.toUByte()), (14.toUByte()), (14.toUByte()), (14.toUByte()), (16.toUByte()), (16.toUByte()), (16.toUByte()), (20.toUByte()), (20.toUByte()), (20.toUByte()), (26.toUByte()), (26.toUByte()), (26.toUByte()), (66.toUByte()), (66.toUByte()), (66.toUByte()), (0.toUByte())), Array40UByteAlloc((4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (6.toUByte()), (6.toUByte()), (6.toUByte()), (8.toUByte()), (8.toUByte()), (8.toUByte()), (12.toUByte()), (12.toUByte()), (12.toUByte()), (16.toUByte()), (16.toUByte()), (16.toUByte()), (20.toUByte()), (20.toUByte()), (20.toUByte()), (26.toUByte()), (26.toUByte()), (26.toUByte()), (34.toUByte()), (34.toUByte()), (34.toUByte()), (42.toUByte()), (42.toUByte()), (42.toUByte()), (12.toUByte()), (12.toUByte()), (12.toUByte()), (0.toUByte())))
    var __STATIC_L3_read_side_info_g_scf_short: Array8Array40UByte = __STATIC_L3_read_side_info_g_scf_short__INIT()
    fun __STATIC_L3_read_side_info_g_scf_mixed__INIT() = Array8Array40UByteAlloc(Array40UByteAlloc((6.toUByte()), (6.toUByte()), (6.toUByte()), (6.toUByte()), (6.toUByte()), (6.toUByte()), (6.toUByte()), (6.toUByte()), (6.toUByte()), (8.toUByte()), (8.toUByte()), (8.toUByte()), (10.toUByte()), (10.toUByte()), (10.toUByte()), (12.toUByte()), (12.toUByte()), (12.toUByte()), (14.toUByte()), (14.toUByte()), (14.toUByte()), (18.toUByte()), (18.toUByte()), (18.toUByte()), (24.toUByte()), (24.toUByte()), (24.toUByte()), (30.toUByte()), (30.toUByte()), (30.toUByte()), (40.toUByte()), (40.toUByte()), (40.toUByte()), (18.toUByte()), (18.toUByte()), (18.toUByte()), (0.toUByte())), Array40UByteAlloc((12.toUByte()), (12.toUByte()), (12.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (8.toUByte()), (8.toUByte()), (8.toUByte()), (12.toUByte()), (12.toUByte()), (12.toUByte()), (16.toUByte()), (16.toUByte()), (16.toUByte()), (20.toUByte()), (20.toUByte()), (20.toUByte()), (24.toUByte()), (24.toUByte()), (24.toUByte()), (28.toUByte()), (28.toUByte()), (28.toUByte()), (36.toUByte()), (36.toUByte()), (36.toUByte()), (2.toUByte()), (2.toUByte()), (2.toUByte()), (2.toUByte()), (2.toUByte()), (2.toUByte()), (2.toUByte()), (2.toUByte()), (2.toUByte()), (26.toUByte()), (26.toUByte()), (26.toUByte()), (0.toUByte())), Array40UByteAlloc((6.toUByte()), (6.toUByte()), (6.toUByte()), (6.toUByte()), (6.toUByte()), (6.toUByte()), (6.toUByte()), (6.toUByte()), (6.toUByte()), (6.toUByte()), (6.toUByte()), (6.toUByte()), (8.toUByte()), (8.toUByte()), (8.toUByte()), (10.toUByte()), (10.toUByte()), (10.toUByte()), (14.toUByte()), (14.toUByte()), (14.toUByte()), (18.toUByte()), (18.toUByte()), (18.toUByte()), (26.toUByte()), (26.toUByte()), (26.toUByte()), (32.toUByte()), (32.toUByte()), (32.toUByte()), (42.toUByte()), (42.toUByte()), (42.toUByte()), (18.toUByte()), (18.toUByte()), (18.toUByte()), (0.toUByte())), Array40UByteAlloc((6.toUByte()), (6.toUByte()), (6.toUByte()), (6.toUByte()), (6.toUByte()), (6.toUByte()), (6.toUByte()), (6.toUByte()), (6.toUByte()), (8.toUByte()), (8.toUByte()), (8.toUByte()), (10.toUByte()), (10.toUByte()), (10.toUByte()), (12.toUByte()), (12.toUByte()), (12.toUByte()), (14.toUByte()), (14.toUByte()), (14.toUByte()), (18.toUByte()), (18.toUByte()), (18.toUByte()), (24.toUByte()), (24.toUByte()), (24.toUByte()), (32.toUByte()), (32.toUByte()), (32.toUByte()), (44.toUByte()), (44.toUByte()), (44.toUByte()), (12.toUByte()), (12.toUByte()), (12.toUByte()), (0.toUByte())), Array40UByteAlloc((6.toUByte()), (6.toUByte()), (6.toUByte()), (6.toUByte()), (6.toUByte()), (6.toUByte()), (6.toUByte()), (6.toUByte()), (6.toUByte()), (8.toUByte()), (8.toUByte()), (8.toUByte()), (10.toUByte()), (10.toUByte()), (10.toUByte()), (12.toUByte()), (12.toUByte()), (12.toUByte()), (14.toUByte()), (14.toUByte()), (14.toUByte()), (18.toUByte()), (18.toUByte()), (18.toUByte()), (24.toUByte()), (24.toUByte()), (24.toUByte()), (30.toUByte()), (30.toUByte()), (30.toUByte()), (40.toUByte()), (40.toUByte()), (40.toUByte()), (18.toUByte()), (18.toUByte()), (18.toUByte()), (0.toUByte())), Array40UByteAlloc((4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (6.toUByte()), (6.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (6.toUByte()), (6.toUByte()), (6.toUByte()), (8.toUByte()), (8.toUByte()), (8.toUByte()), (10.toUByte()), (10.toUByte()), (10.toUByte()), (12.toUByte()), (12.toUByte()), (12.toUByte()), (14.toUByte()), (14.toUByte()), (14.toUByte()), (18.toUByte()), (18.toUByte()), (18.toUByte()), (22.toUByte()), (22.toUByte()), (22.toUByte()), (30.toUByte()), (30.toUByte()), (30.toUByte()), (56.toUByte()), (56.toUByte()), (56.toUByte()), (0.toUByte())), Array40UByteAlloc((4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (6.toUByte()), (6.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (6.toUByte()), (6.toUByte()), (6.toUByte()), (6.toUByte()), (6.toUByte()), (6.toUByte()), (10.toUByte()), (10.toUByte()), (10.toUByte()), (12.toUByte()), (12.toUByte()), (12.toUByte()), (14.toUByte()), (14.toUByte()), (14.toUByte()), (16.toUByte()), (16.toUByte()), (16.toUByte()), (20.toUByte()), (20.toUByte()), (20.toUByte()), (26.toUByte()), (26.toUByte()), (26.toUByte()), (66.toUByte()), (66.toUByte()), (66.toUByte()), (0.toUByte())), Array40UByteAlloc((4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (6.toUByte()), (6.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (6.toUByte()), (6.toUByte()), (6.toUByte()), (8.toUByte()), (8.toUByte()), (8.toUByte()), (12.toUByte()), (12.toUByte()), (12.toUByte()), (16.toUByte()), (16.toUByte()), (16.toUByte()), (20.toUByte()), (20.toUByte()), (20.toUByte()), (26.toUByte()), (26.toUByte()), (26.toUByte()), (34.toUByte()), (34.toUByte()), (34.toUByte()), (42.toUByte()), (42.toUByte()), (42.toUByte()), (12.toUByte()), (12.toUByte()), (12.toUByte()), (0.toUByte())))
    var __STATIC_L3_read_side_info_g_scf_mixed: Array8Array40UByte = __STATIC_L3_read_side_info_g_scf_mixed__INIT()
    fun __STATIC_L3_ldexp_q2_g_expfrac__INIT() = Array4FloatAlloc(9.31322575e-10f, 7.83145814e-10f, 6.58544508e-10f, 5.53767716e-10f)
    var __STATIC_L3_ldexp_q2_g_expfrac: Array4Float = __STATIC_L3_ldexp_q2_g_expfrac__INIT()
    fun __STATIC_L3_decode_scalefactors_g_scf_partitions__INIT() = Array3Array28UByteAlloc(Array28UByteAlloc((6.toUByte()), (5.toUByte()), (5.toUByte()), (5.toUByte()), (6.toUByte()), (5.toUByte()), (5.toUByte()), (5.toUByte()), (6.toUByte()), (5.toUByte()), (7.toUByte()), (3.toUByte()), (11.toUByte()), (10.toUByte()), (0.toUByte()), (0.toUByte()), (7.toUByte()), (7.toUByte()), (7.toUByte()), (0.toUByte()), (6.toUByte()), (6.toUByte()), (6.toUByte()), (3.toUByte()), (8.toUByte()), (8.toUByte()), (5.toUByte()), (0.toUByte())), Array28UByteAlloc((8.toUByte()), (9.toUByte()), (6.toUByte()), (12.toUByte()), (6.toUByte()), (9.toUByte()), (9.toUByte()), (9.toUByte()), (6.toUByte()), (9.toUByte()), (12.toUByte()), (6.toUByte()), (15.toUByte()), (18.toUByte()), (0.toUByte()), (0.toUByte()), (6.toUByte()), (15.toUByte()), (12.toUByte()), (0.toUByte()), (6.toUByte()), (12.toUByte()), (9.toUByte()), (6.toUByte()), (6.toUByte()), (18.toUByte()), (9.toUByte()), (0.toUByte())), Array28UByteAlloc((9.toUByte()), (9.toUByte()), (6.toUByte()), (12.toUByte()), (9.toUByte()), (9.toUByte()), (9.toUByte()), (9.toUByte()), (9.toUByte()), (9.toUByte()), (12.toUByte()), (6.toUByte()), (18.toUByte()), (18.toUByte()), (0.toUByte()), (0.toUByte()), (12.toUByte()), (12.toUByte()), (12.toUByte()), (0.toUByte()), (12.toUByte()), (9.toUByte()), (9.toUByte()), (6.toUByte()), (15.toUByte()), (12.toUByte()), (9.toUByte()), (0.toUByte())))
    var __STATIC_L3_decode_scalefactors_g_scf_partitions: Array3Array28UByte = __STATIC_L3_decode_scalefactors_g_scf_partitions__INIT()
    fun __STATIC_L3_decode_scalefactors_g_scfc_decode__INIT() = Array16UByteAlloc((0.toUByte()), (1.toUByte()), (2.toUByte()), (3.toUByte()), (12.toUByte()), (5.toUByte()), (6.toUByte()), (7.toUByte()), (9.toUByte()), (10.toUByte()), (11.toUByte()), (13.toUByte()), (14.toUByte()), (15.toUByte()), (18.toUByte()), (19.toUByte()))
    var __STATIC_L3_decode_scalefactors_g_scfc_decode: Array16UByte = __STATIC_L3_decode_scalefactors_g_scfc_decode__INIT()
    fun __STATIC_L3_decode_scalefactors_g_mod__INIT() = Array24UByteAlloc((5.toUByte()), (5.toUByte()), (4.toUByte()), (4.toUByte()), (5.toUByte()), (5.toUByte()), (4.toUByte()), (1.toUByte()), (4.toUByte()), (3.toUByte()), (1.toUByte()), (1.toUByte()), (5.toUByte()), (6.toUByte()), (6.toUByte()), (1.toUByte()), (4.toUByte()), (4.toUByte()), (4.toUByte()), (1.toUByte()), (4.toUByte()), (3.toUByte()), (1.toUByte()), (1.toUByte()))
    var __STATIC_L3_decode_scalefactors_g_mod: Array24UByte = __STATIC_L3_decode_scalefactors_g_mod__INIT()
    fun __STATIC_L3_decode_scalefactors_g_preamp__INIT() = Array10UByteAlloc((1.toUByte()), (1.toUByte()), (1.toUByte()), (1.toUByte()), (2.toUByte()), (2.toUByte()), (3.toUByte()), (3.toUByte()), (3.toUByte()), (2.toUByte()))
    var __STATIC_L3_decode_scalefactors_g_preamp: Array10UByte = __STATIC_L3_decode_scalefactors_g_preamp__INIT()
    fun __STATIC_L3_huffman_tabs__INIT() = fixedArrayOfShort(2164, (0.toShort()), (0.toShort()), (0.toShort()), (0.toShort()), (0.toShort()), (0.toShort()), (0.toShort()), (0.toShort()), (0.toShort()), (0.toShort()), (0.toShort()), (0.toShort()), (0.toShort()), (0.toShort()), (0.toShort()), (0.toShort()), (0.toShort()), (0.toShort()), (0.toShort()), (0.toShort()), (0.toShort()), (0.toShort()), (0.toShort()), (0.toShort()), (0.toShort()), (0.toShort()), (0.toShort()), (0.toShort()), (0.toShort()), (0.toShort()), (0.toShort()), (0.toShort()), (785.toShort()), (785.toShort()), (785.toShort()), (785.toShort()), (784.toShort()), (784.toShort()), (784.toShort()), (784.toShort()), (513.toShort()), (513.toShort()), (513.toShort()), (513.toShort()), (513.toShort()), (513.toShort()), (513.toShort()), (513.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), ((-255).toShort()), (1313.toShort()), (1298.toShort()), (1282.toShort()), (785.toShort()), (785.toShort()), (785.toShort()), (785.toShort()), (784.toShort()), (784.toShort()), (784.toShort()), (784.toShort()), (769.toShort()), (769.toShort()), (769.toShort()), (769.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (290.toShort()), (288.toShort()), ((-255).toShort()), (1313.toShort()), (1298.toShort()), (1282.toShort()), (769.toShort()), (769.toShort()), (769.toShort()), (769.toShort()), (529.toShort()), (529.toShort()), (529.toShort()), (529.toShort()), (529.toShort()), (529.toShort()), (529.toShort()), (529.toShort()), (528.toShort()), (528.toShort()), (528.toShort()), (528.toShort()), (528.toShort()), (528.toShort()), (528.toShort()), (528.toShort()), (512.toShort()), (512.toShort()), (512.toShort()), (512.toShort()), (512.toShort()), (512.toShort()), (512.toShort()), (512.toShort()), (290.toShort()), (288.toShort()), ((-253).toShort()), ((-318).toShort()), ((-351).toShort()), ((-367).toShort()), (785.toShort()), (785.toShort()), (785.toShort()), (785.toShort()), (784.toShort()), (784.toShort()), (784.toShort()), (784.toShort()), (769.toShort()), (769.toShort()), (769.toShort()), (769.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (819.toShort()), (818.toShort()), (547.toShort()), (547.toShort()), (275.toShort()), (275.toShort()), (275.toShort()), (275.toShort()), (561.toShort()), (560.toShort()), (515.toShort()), (546.toShort()), (289.toShort()), (274.toShort()), (288.toShort()), (258.toShort()), ((-254).toShort()), ((-287).toShort()), (1329.toShort()), (1299.toShort()), (1314.toShort()), (1312.toShort()), (1057.toShort()), (1057.toShort()), (1042.toShort()), (1042.toShort()), (1026.toShort()), (1026.toShort()), (784.toShort()), (784.toShort()), (784.toShort()), (784.toShort()), (529.toShort()), (529.toShort()), (529.toShort()), (529.toShort()), (529.toShort()), (529.toShort()), (529.toShort()), (529.toShort()), (769.toShort()), (769.toShort()), (769.toShort()), (769.toShort()), (768.toShort()), (768.toShort()), (768.toShort()), (768.toShort()), (563.toShort()), (560.toShort()), (306.toShort()), (306.toShort()), (291.toShort()), (259.toShort()), ((-252).toShort()), ((-413).toShort()), ((-477).toShort()), ((-542).toShort()), (1298.toShort()), ((-575).toShort()), (1041.toShort()), (1041.toShort()), (784.toShort()), (784.toShort()), (784.toShort()), (784.toShort()), (769.toShort()), (769.toShort()), (769.toShort()), (769.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), ((-383).toShort()), ((-399).toShort()), (1107.toShort()), (1092.toShort()), (1106.toShort()), (1061.toShort()), (849.toShort()), (849.toShort()), (789.toShort()), (789.toShort()), (1104.toShort()), (1091.toShort()), (773.toShort()), (773.toShort()), (1076.toShort()), (1075.toShort()), (341.toShort()), (340.toShort()), (325.toShort()), (309.toShort()), (834.toShort()), (804.toShort()), (577.toShort()), (577.toShort()), (532.toShort()), (532.toShort()), (516.toShort()), (516.toShort()), (832.toShort()), (818.toShort()), (803.toShort()), (816.toShort()), (561.toShort()), (561.toShort()), (531.toShort()), (531.toShort()), (515.toShort()), (546.toShort()), (289.toShort()), (289.toShort()), (288.toShort()), (258.toShort()), ((-252).toShort()), ((-429).toShort()), ((-493).toShort()), ((-559).toShort()), (1057.toShort()), (1057.toShort()), (1042.toShort()), (1042.toShort()), (529.toShort()), (529.toShort()), (529.toShort()), (529.toShort()), (529.toShort()), (529.toShort()), (529.toShort()), (529.toShort()), (784.toShort()), (784.toShort()), (784.toShort()), (784.toShort()), (769.toShort()), (769.toShort()), (769.toShort()), (769.toShort()), (512.toShort()), (512.toShort()), (512.toShort()), (512.toShort()), (512.toShort()), (512.toShort()), (512.toShort()), (512.toShort()), ((-382).toShort()), (1077.toShort()), ((-415).toShort()), (1106.toShort()), (1061.toShort()), (1104.toShort()), (849.toShort()), (849.toShort()), (789.toShort()), (789.toShort()), (1091.toShort()), (1076.toShort()), (1029.toShort()), (1075.toShort()), (834.toShort()), (834.toShort()), (597.toShort()), (581.toShort()), (340.toShort()), (340.toShort()), (339.toShort()), (324.toShort()), (804.toShort()), (833.toShort()), (532.toShort()), (532.toShort()), (832.toShort()), (772.toShort()), (818.toShort()), (803.toShort()), (817.toShort()), (787.toShort()), (816.toShort()), (771.toShort()), (290.toShort()), (290.toShort()), (290.toShort()), (290.toShort()), (288.toShort()), (258.toShort()), ((-253).toShort()), ((-349).toShort()), ((-414).toShort()), ((-447).toShort()), ((-463).toShort()), (1329.toShort()), (1299.toShort()), ((-479).toShort()), (1314.toShort()), (1312.toShort()), (1057.toShort()), (1057.toShort()), (1042.toShort()), (1042.toShort()), (1026.toShort()), (1026.toShort()), (785.toShort()), (785.toShort()), (785.toShort()), (785.toShort()), (784.toShort()), (784.toShort()), (784.toShort()), (784.toShort()), (769.toShort()), (769.toShort()), (769.toShort()), (769.toShort()), (768.toShort()), (768.toShort()), (768.toShort()), (768.toShort()), ((-319).toShort()), (851.toShort()), (821.toShort()), ((-335).toShort()), (836.toShort()), (850.toShort()), (805.toShort()), (849.toShort()), (341.toShort()), (340.toShort()), (325.toShort()), (336.toShort()), (533.toShort()), (533.toShort()), (579.toShort()), (579.toShort()), (564.toShort()), (564.toShort()), (773.toShort()), (832.toShort()), (578.toShort()), (548.toShort()), (563.toShort()), (516.toShort()), (321.toShort()), (276.toShort()), (306.toShort()), (291.toShort()), (304.toShort()), (259.toShort()), ((-251).toShort()), ((-572).toShort()), ((-733).toShort()), ((-830).toShort()), ((-863).toShort()), ((-879).toShort()), (1041.toShort()), (1041.toShort()), (784.toShort()), (784.toShort()), (784.toShort()), (784.toShort()), (769.toShort()), (769.toShort()), (769.toShort()), (769.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), ((-511).toShort()), ((-527).toShort()), ((-543).toShort()), (1396.toShort()), (1351.toShort()), (1381.toShort()), (1366.toShort()), (1395.toShort()), (1335.toShort()), (1380.toShort()), ((-559).toShort()), (1334.toShort()), (1138.toShort()), (1138.toShort()), (1063.toShort()), (1063.toShort()), (1350.toShort()), (1392.toShort()), (1031.toShort()), (1031.toShort()), (1062.toShort()), (1062.toShort()), (1364.toShort()), (1363.toShort()), (1120.toShort()), (1120.toShort()), (1333.toShort()), (1348.toShort()), (881.toShort()), (881.toShort()), (881.toShort()), (881.toShort()), (375.toShort()), (374.toShort()), (359.toShort()), (373.toShort()), (343.toShort()), (358.toShort()), (341.toShort()), (325.toShort()), (791.toShort()), (791.toShort()), (1123.toShort()), (1122.toShort()), ((-703).toShort()), (1105.toShort()), (1045.toShort()), ((-719).toShort()), (865.toShort()), (865.toShort()), (790.toShort()), (790.toShort()), (774.toShort()), (774.toShort()), (1104.toShort()), (1029.toShort()), (338.toShort()), (293.toShort()), (323.toShort()), (308.toShort()), ((-799).toShort()), ((-815).toShort()), (833.toShort()), (788.toShort()), (772.toShort()), (818.toShort()), (803.toShort()), (816.toShort()), (322.toShort()), (292.toShort()), (307.toShort()), (320.toShort()), (561.toShort()), (531.toShort()), (515.toShort()), (546.toShort()), (289.toShort()), (274.toShort()), (288.toShort()), (258.toShort()), ((-251).toShort()), ((-525).toShort()), ((-605).toShort()), ((-685).toShort()), ((-765).toShort()), ((-831).toShort()), ((-846).toShort()), (1298.toShort()), (1057.toShort()), (1057.toShort()), (1312.toShort()), (1282.toShort()), (785.toShort()), (785.toShort()), (785.toShort()), (785.toShort()), (784.toShort()), (784.toShort()), (784.toShort()), (784.toShort()), (769.toShort()), (769.toShort()), (769.toShort()), (769.toShort()), (512.toShort()), (512.toShort()), (512.toShort()), (512.toShort()), (512.toShort()), (512.toShort()), (512.toShort()), (512.toShort()), (1399.toShort()), (1398.toShort()), (1383.toShort()), (1367.toShort()), (1382.toShort()), (1396.toShort()), (1351.toShort()), ((-511).toShort()), (1381.toShort()), (1366.toShort()), (1139.toShort()), (1139.toShort()), (1079.toShort()), (1079.toShort()), (1124.toShort()), (1124.toShort()), (1364.toShort()), (1349.toShort()), (1363.toShort()), (1333.toShort()), (882.toShort()), (882.toShort()), (882.toShort()), (882.toShort()), (807.toShort()), (807.toShort()), (807.toShort()), (807.toShort()), (1094.toShort()), (1094.toShort()), (1136.toShort()), (1136.toShort()), (373.toShort()), (341.toShort()), (535.toShort()), (535.toShort()), (881.toShort()), (775.toShort()), (867.toShort()), (822.toShort()), (774.toShort()), ((-591).toShort()), (324.toShort()), (338.toShort()), ((-671).toShort()), (849.toShort()), (550.toShort()), (550.toShort()), (866.toShort()), (864.toShort()), (609.toShort()), (609.toShort()), (293.toShort()), (336.toShort()), (534.toShort()), (534.toShort()), (789.toShort()), (835.toShort()), (773.toShort()), ((-751).toShort()), (834.toShort()), (804.toShort()), (308.toShort()), (307.toShort()), (833.toShort()), (788.toShort()), (832.toShort()), (772.toShort()), (562.toShort()), (562.toShort()), (547.toShort()), (547.toShort()), (305.toShort()), (275.toShort()), (560.toShort()), (515.toShort()), (290.toShort()), (290.toShort()), ((-252).toShort()), ((-397).toShort()), ((-477).toShort()), ((-557).toShort()), ((-622).toShort()), ((-653).toShort()), ((-719).toShort()), ((-735).toShort()), ((-750).toShort()), (1329.toShort()), (1299.toShort()), (1314.toShort()), (1057.toShort()), (1057.toShort()), (1042.toShort()), (1042.toShort()), (1312.toShort()), (1282.toShort()), (1024.toShort()), (1024.toShort()), (785.toShort()), (785.toShort()), (785.toShort()), (785.toShort()), (784.toShort()), (784.toShort()), (784.toShort()), (784.toShort()), (769.toShort()), (769.toShort()), (769.toShort()), (769.toShort()), ((-383).toShort()), (1127.toShort()), (1141.toShort()), (1111.toShort()), (1126.toShort()), (1140.toShort()), (1095.toShort()), (1110.toShort()), (869.toShort()), (869.toShort()), (883.toShort()), (883.toShort()), (1079.toShort()), (1109.toShort()), (882.toShort()), (882.toShort()), (375.toShort()), (374.toShort()), (807.toShort()), (868.toShort()), (838.toShort()), (881.toShort()), (791.toShort()), ((-463).toShort()), (867.toShort()), (822.toShort()), (368.toShort()), (263.toShort()), (852.toShort()), (837.toShort()), (836.toShort()), ((-543).toShort()), (610.toShort()), (610.toShort()), (550.toShort()), (550.toShort()), (352.toShort()), (336.toShort()), (534.toShort()), (534.toShort()), (865.toShort()), (774.toShort()), (851.toShort()), (821.toShort()), (850.toShort()), (805.toShort()), (593.toShort()), (533.toShort()), (579.toShort()), (564.toShort()), (773.toShort()), (832.toShort()), (578.toShort()), (578.toShort()), (548.toShort()), (548.toShort()), (577.toShort()), (577.toShort()), (307.toShort()), (276.toShort()), (306.toShort()), (291.toShort()), (516.toShort()), (560.toShort()), (259.toShort()), (259.toShort()), ((-250).toShort()), ((-2107).toShort()), ((-2507).toShort()), ((-2764).toShort()), ((-2909).toShort()), ((-2974).toShort()), ((-3007).toShort()), ((-3023).toShort()), (1041.toShort()), (1041.toShort()), (1040.toShort()), (1040.toShort()), (769.toShort()), (769.toShort()), (769.toShort()), (769.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), ((-767).toShort()), ((-1052).toShort()), ((-1213).toShort()), ((-1277).toShort()), ((-1358).toShort()), ((-1405).toShort()), ((-1469).toShort()), ((-1535).toShort()), ((-1550).toShort()), ((-1582).toShort()), ((-1614).toShort()), ((-1647).toShort()), ((-1662).toShort()), ((-1694).toShort()), ((-1726).toShort()), ((-1759).toShort()), ((-1774).toShort()), ((-1807).toShort()), ((-1822).toShort()), ((-1854).toShort()), ((-1886).toShort()), (1565.toShort()), ((-1919).toShort()), ((-1935).toShort()), ((-1951).toShort()), ((-1967).toShort()), (1731.toShort()), (1730.toShort()), (1580.toShort()), (1717.toShort()), ((-1983).toShort()), (1729.toShort()), (1564.toShort()), ((-1999).toShort()), (1548.toShort()), ((-2015).toShort()), ((-2031).toShort()), (1715.toShort()), (1595.toShort()), ((-2047).toShort()), (1714.toShort()), ((-2063).toShort()), (1610.toShort()), ((-2079).toShort()), (1609.toShort()), ((-2095).toShort()), (1323.toShort()), (1323.toShort()), (1457.toShort()), (1457.toShort()), (1307.toShort()), (1307.toShort()), (1712.toShort()), (1547.toShort()), (1641.toShort()), (1700.toShort()), (1699.toShort()), (1594.toShort()), (1685.toShort()), (1625.toShort()), (1442.toShort()), (1442.toShort()), (1322.toShort()), (1322.toShort()), ((-780).toShort()), ((-973).toShort()), ((-910).toShort()), (1279.toShort()), (1278.toShort()), (1277.toShort()), (1262.toShort()), (1276.toShort()), (1261.toShort()), (1275.toShort()), (1215.toShort()), (1260.toShort()), (1229.toShort()), ((-959).toShort()), (974.toShort()), (974.toShort()), (989.toShort()), (989.toShort()), ((-943).toShort()), (735.toShort()), (478.toShort()), (478.toShort()), (495.toShort()), (463.toShort()), (506.toShort()), (414.toShort()), ((-1039).toShort()), (1003.toShort()), (958.toShort()), (1017.toShort()), (927.toShort()), (942.toShort()), (987.toShort()), (957.toShort()), (431.toShort()), (476.toShort()), (1272.toShort()), (1167.toShort()), (1228.toShort()), ((-1183).toShort()), (1256.toShort()), ((-1199).toShort()), (895.toShort()), (895.toShort()), (941.toShort()), (941.toShort()), (1242.toShort()), (1227.toShort()), (1212.toShort()), (1135.toShort()), (1014.toShort()), (1014.toShort()), (490.toShort()), (489.toShort()), (503.toShort()), (487.toShort()), (910.toShort()), (1013.toShort()), (985.toShort()), (925.toShort()), (863.toShort()), (894.toShort()), (970.toShort()), (955.toShort()), (1012.toShort()), (847.toShort()), ((-1343).toShort()), (831.toShort()), (755.toShort()), (755.toShort()), (984.toShort()), (909.toShort()), (428.toShort()), (366.toShort()), (754.toShort()), (559.toShort()), ((-1391).toShort()), (752.toShort()), (486.toShort()), (457.toShort()), (924.toShort()), (997.toShort()), (698.toShort()), (698.toShort()), (983.toShort()), (893.toShort()), (740.toShort()), (740.toShort()), (908.toShort()), (877.toShort()), (739.toShort()), (739.toShort()), (667.toShort()), (667.toShort()), (953.toShort()), (938.toShort()), (497.toShort()), (287.toShort()), (271.toShort()), (271.toShort()), (683.toShort()), (606.toShort()), (590.toShort()), (712.toShort()), (726.toShort()), (574.toShort()), (302.toShort()), (302.toShort()), (738.toShort()), (736.toShort()), (481.toShort()), (286.toShort()), (526.toShort()), (725.toShort()), (605.toShort()), (711.toShort()), (636.toShort()), (724.toShort()), (696.toShort()), (651.toShort()), (589.toShort()), (681.toShort()), (666.toShort()), (710.toShort()), (364.toShort()), (467.toShort()), (573.toShort()), (695.toShort()), (466.toShort()), (466.toShort()), (301.toShort()), (465.toShort()), (379.toShort()), (379.toShort()), (709.toShort()), (604.toShort()), (665.toShort()), (679.toShort()), (316.toShort()), (316.toShort()), (634.toShort()), (633.toShort()), (436.toShort()), (436.toShort()), (464.toShort()), (269.toShort()), (424.toShort()), (394.toShort()), (452.toShort()), (332.toShort()), (438.toShort()), (363.toShort()), (347.toShort()), (408.toShort()), (393.toShort()), (448.toShort()), (331.toShort()), (422.toShort()), (362.toShort()), (407.toShort()), (392.toShort()), (421.toShort()), (346.toShort()), (406.toShort()), (391.toShort()), (376.toShort()), (375.toShort()), (359.toShort()), (1441.toShort()), (1306.toShort()), ((-2367).toShort()), (1290.toShort()), ((-2383).toShort()), (1337.toShort()), ((-2399).toShort()), ((-2415).toShort()), (1426.toShort()), (1321.toShort()), ((-2431).toShort()), (1411.toShort()), (1336.toShort()), ((-2447).toShort()), ((-2463).toShort()), ((-2479).toShort()), (1169.toShort()), (1169.toShort()), (1049.toShort()), (1049.toShort()), (1424.toShort()), (1289.toShort()), (1412.toShort()), (1352.toShort()), (1319.toShort()), ((-2495).toShort()), (1154.toShort()), (1154.toShort()), (1064.toShort()), (1064.toShort()), (1153.toShort()), (1153.toShort()), (416.toShort()), (390.toShort()), (360.toShort()), (404.toShort()), (403.toShort()), (389.toShort()), (344.toShort()), (374.toShort()), (373.toShort()), (343.toShort()), (358.toShort()), (372.toShort()), (327.toShort()), (357.toShort()), (342.toShort()), (311.toShort()), (356.toShort()), (326.toShort()), (1395.toShort()), (1394.toShort()), (1137.toShort()), (1137.toShort()), (1047.toShort()), (1047.toShort()), (1365.toShort()), (1392.toShort()), (1287.toShort()), (1379.toShort()), (1334.toShort()), (1364.toShort()), (1349.toShort()), (1378.toShort()), (1318.toShort()), (1363.toShort()), (792.toShort()), (792.toShort()), (792.toShort()), (792.toShort()), (1152.toShort()), (1152.toShort()), (1032.toShort()), (1032.toShort()), (1121.toShort()), (1121.toShort()), (1046.toShort()), (1046.toShort()), (1120.toShort()), (1120.toShort()), (1030.toShort()), (1030.toShort()), ((-2895).toShort()), (1106.toShort()), (1061.toShort()), (1104.toShort()), (849.toShort()), (849.toShort()), (789.toShort()), (789.toShort()), (1091.toShort()), (1076.toShort()), (1029.toShort()), (1090.toShort()), (1060.toShort()), (1075.toShort()), (833.toShort()), (833.toShort()), (309.toShort()), (324.toShort()), (532.toShort()), (532.toShort()), (832.toShort()), (772.toShort()), (818.toShort()), (803.toShort()), (561.toShort()), (561.toShort()), (531.toShort()), (560.toShort()), (515.toShort()), (546.toShort()), (289.toShort()), (274.toShort()), (288.toShort()), (258.toShort()), ((-250).toShort()), ((-1179).toShort()), ((-1579).toShort()), ((-1836).toShort()), ((-1996).toShort()), ((-2124).toShort()), ((-2253).toShort()), ((-2333).toShort()), ((-2413).toShort()), ((-2477).toShort()), ((-2542).toShort()), ((-2574).toShort()), ((-2607).toShort()), ((-2622).toShort()), ((-2655).toShort()), (1314.toShort()), (1313.toShort()), (1298.toShort()), (1312.toShort()), (1282.toShort()), (785.toShort()), (785.toShort()), (785.toShort()), (785.toShort()), (1040.toShort()), (1040.toShort()), (1025.toShort()), (1025.toShort()), (768.toShort()), (768.toShort()), (768.toShort()), (768.toShort()), ((-766).toShort()), ((-798).toShort()), ((-830).toShort()), ((-862).toShort()), ((-895).toShort()), ((-911).toShort()), ((-927).toShort()), ((-943).toShort()), ((-959).toShort()), ((-975).toShort()), ((-991).toShort()), ((-1007).toShort()), ((-1023).toShort()), ((-1039).toShort()), ((-1055).toShort()), ((-1070).toShort()), (1724.toShort()), (1647.toShort()), ((-1103).toShort()), ((-1119).toShort()), (1631.toShort()), (1767.toShort()), (1662.toShort()), (1738.toShort()), (1708.toShort()), (1723.toShort()), ((-1135).toShort()), (1780.toShort()), (1615.toShort()), (1779.toShort()), (1599.toShort()), (1677.toShort()), (1646.toShort()), (1778.toShort()), (1583.toShort()), ((-1151).toShort()), (1777.toShort()), (1567.toShort()), (1737.toShort()), (1692.toShort()), (1765.toShort()), (1722.toShort()), (1707.toShort()), (1630.toShort()), (1751.toShort()), (1661.toShort()), (1764.toShort()), (1614.toShort()), (1736.toShort()), (1676.toShort()), (1763.toShort()), (1750.toShort()), (1645.toShort()), (1598.toShort()), (1721.toShort()), (1691.toShort()), (1762.toShort()), (1706.toShort()), (1582.toShort()), (1761.toShort()), (1566.toShort()), ((-1167).toShort()), (1749.toShort()), (1629.toShort()), (767.toShort()), (766.toShort()), (751.toShort()), (765.toShort()), (494.toShort()), (494.toShort()), (735.toShort()), (764.toShort()), (719.toShort()), (749.toShort()), (734.toShort()), (763.toShort()), (447.toShort()), (447.toShort()), (748.toShort()), (718.toShort()), (477.toShort()), (506.toShort()), (431.toShort()), (491.toShort()), (446.toShort()), (476.toShort()), (461.toShort()), (505.toShort()), (415.toShort()), (430.toShort()), (475.toShort()), (445.toShort()), (504.toShort()), (399.toShort()), (460.toShort()), (489.toShort()), (414.toShort()), (503.toShort()), (383.toShort()), (474.toShort()), (429.toShort()), (459.toShort()), (502.toShort()), (502.toShort()), (746.toShort()), (752.toShort()), (488.toShort()), (398.toShort()), (501.toShort()), (473.toShort()), (413.toShort()), (472.toShort()), (486.toShort()), (271.toShort()), (480.toShort()), (270.toShort()), ((-1439).toShort()), ((-1455).toShort()), (1357.toShort()), ((-1471).toShort()), ((-1487).toShort()), ((-1503).toShort()), (1341.toShort()), (1325.toShort()), ((-1519).toShort()), (1489.toShort()), (1463.toShort()), (1403.toShort()), (1309.toShort()), ((-1535).toShort()), (1372.toShort()), (1448.toShort()), (1418.toShort()), (1476.toShort()), (1356.toShort()), (1462.toShort()), (1387.toShort()), ((-1551).toShort()), (1475.toShort()), (1340.toShort()), (1447.toShort()), (1402.toShort()), (1386.toShort()), ((-1567).toShort()), (1068.toShort()), (1068.toShort()), (1474.toShort()), (1461.toShort()), (455.toShort()), (380.toShort()), (468.toShort()), (440.toShort()), (395.toShort()), (425.toShort()), (410.toShort()), (454.toShort()), (364.toShort()), (467.toShort()), (466.toShort()), (464.toShort()), (453.toShort()), (269.toShort()), (409.toShort()), (448.toShort()), (268.toShort()), (432.toShort()), (1371.toShort()), (1473.toShort()), (1432.toShort()), (1417.toShort()), (1308.toShort()), (1460.toShort()), (1355.toShort()), (1446.toShort()), (1459.toShort()), (1431.toShort()), (1083.toShort()), (1083.toShort()), (1401.toShort()), (1416.toShort()), (1458.toShort()), (1445.toShort()), (1067.toShort()), (1067.toShort()), (1370.toShort()), (1457.toShort()), (1051.toShort()), (1051.toShort()), (1291.toShort()), (1430.toShort()), (1385.toShort()), (1444.toShort()), (1354.toShort()), (1415.toShort()), (1400.toShort()), (1443.toShort()), (1082.toShort()), (1082.toShort()), (1173.toShort()), (1113.toShort()), (1186.toShort()), (1066.toShort()), (1185.toShort()), (1050.toShort()), ((-1967).toShort()), (1158.toShort()), (1128.toShort()), (1172.toShort()), (1097.toShort()), (1171.toShort()), (1081.toShort()), ((-1983).toShort()), (1157.toShort()), (1112.toShort()), (416.toShort()), (266.toShort()), (375.toShort()), (400.toShort()), (1170.toShort()), (1142.toShort()), (1127.toShort()), (1065.toShort()), (793.toShort()), (793.toShort()), (1169.toShort()), (1033.toShort()), (1156.toShort()), (1096.toShort()), (1141.toShort()), (1111.toShort()), (1155.toShort()), (1080.toShort()), (1126.toShort()), (1140.toShort()), (898.toShort()), (898.toShort()), (808.toShort()), (808.toShort()), (897.toShort()), (897.toShort()), (792.toShort()), (792.toShort()), (1095.toShort()), (1152.toShort()), (1032.toShort()), (1125.toShort()), (1110.toShort()), (1139.toShort()), (1079.toShort()), (1124.toShort()), (882.toShort()), (807.toShort()), (838.toShort()), (881.toShort()), (853.toShort()), (791.toShort()), ((-2319).toShort()), (867.toShort()), (368.toShort()), (263.toShort()), (822.toShort()), (852.toShort()), (837.toShort()), (866.toShort()), (806.toShort()), (865.toShort()), ((-2399).toShort()), (851.toShort()), (352.toShort()), (262.toShort()), (534.toShort()), (534.toShort()), (821.toShort()), (836.toShort()), (594.toShort()), (594.toShort()), (549.toShort()), (549.toShort()), (593.toShort()), (593.toShort()), (533.toShort()), (533.toShort()), (848.toShort()), (773.toShort()), (579.toShort()), (579.toShort()), (564.toShort()), (578.toShort()), (548.toShort()), (563.toShort()), (276.toShort()), (276.toShort()), (577.toShort()), (576.toShort()), (306.toShort()), (291.toShort()), (516.toShort()), (560.toShort()), (305.toShort()), (305.toShort()), (275.toShort()), (259.toShort()), ((-251).toShort()), ((-892).toShort()), ((-2058).toShort()), ((-2620).toShort()), ((-2828).toShort()), ((-2957).toShort()), ((-3023).toShort()), ((-3039).toShort()), (1041.toShort()), (1041.toShort()), (1040.toShort()), (1040.toShort()), (769.toShort()), (769.toShort()), (769.toShort()), (769.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), (256.toShort()), ((-511).toShort()), ((-527).toShort()), ((-543).toShort()), ((-559).toShort()), (1530.toShort()), ((-575).toShort()), ((-591).toShort()), (1528.toShort()), (1527.toShort()), (1407.toShort()), (1526.toShort()), (1391.toShort()), (1023.toShort()), (1023.toShort()), (1023.toShort()), (1023.toShort()), (1525.toShort()), (1375.toShort()), (1268.toShort()), (1268.toShort()), (1103.toShort()), (1103.toShort()), (1087.toShort()), (1087.toShort()), (1039.toShort()), (1039.toShort()), (1523.toShort()), ((-604).toShort()), (815.toShort()), (815.toShort()), (815.toShort()), (815.toShort()), (510.toShort()), (495.toShort()), (509.toShort()), (479.toShort()), (508.toShort()), (463.toShort()), (507.toShort()), (447.toShort()), (431.toShort()), (505.toShort()), (415.toShort()), (399.toShort()), ((-734).toShort()), ((-782).toShort()), (1262.toShort()), ((-815).toShort()), (1259.toShort()), (1244.toShort()), ((-831).toShort()), (1258.toShort()), (1228.toShort()), ((-847).toShort()), ((-863).toShort()), (1196.toShort()), ((-879).toShort()), (1253.toShort()), (987.toShort()), (987.toShort()), (748.toShort()), ((-767).toShort()), (493.toShort()), (493.toShort()), (462.toShort()), (477.toShort()), (414.toShort()), (414.toShort()), (686.toShort()), (669.toShort()), (478.toShort()), (446.toShort()), (461.toShort()), (445.toShort()), (474.toShort()), (429.toShort()), (487.toShort()), (458.toShort()), (412.toShort()), (471.toShort()), (1266.toShort()), (1264.toShort()), (1009.toShort()), (1009.toShort()), (799.toShort()), (799.toShort()), ((-1019).toShort()), ((-1276).toShort()), ((-1452).toShort()), ((-1581).toShort()), ((-1677).toShort()), ((-1757).toShort()), ((-1821).toShort()), ((-1886).toShort()), ((-1933).toShort()), ((-1997).toShort()), (1257.toShort()), (1257.toShort()), (1483.toShort()), (1468.toShort()), (1512.toShort()), (1422.toShort()), (1497.toShort()), (1406.toShort()), (1467.toShort()), (1496.toShort()), (1421.toShort()), (1510.toShort()), (1134.toShort()), (1134.toShort()), (1225.toShort()), (1225.toShort()), (1466.toShort()), (1451.toShort()), (1374.toShort()), (1405.toShort()), (1252.toShort()), (1252.toShort()), (1358.toShort()), (1480.toShort()), (1164.toShort()), (1164.toShort()), (1251.toShort()), (1251.toShort()), (1238.toShort()), (1238.toShort()), (1389.toShort()), (1465.toShort()), ((-1407).toShort()), (1054.toShort()), (1101.toShort()), ((-1423).toShort()), (1207.toShort()), ((-1439).toShort()), (830.toShort()), (830.toShort()), (1248.toShort()), (1038.toShort()), (1237.toShort()), (1117.toShort()), (1223.toShort()), (1148.toShort()), (1236.toShort()), (1208.toShort()), (411.toShort()), (426.toShort()), (395.toShort()), (410.toShort()), (379.toShort()), (269.toShort()), (1193.toShort()), (1222.toShort()), (1132.toShort()), (1235.toShort()), (1221.toShort()), (1116.toShort()), (976.toShort()), (976.toShort()), (1192.toShort()), (1162.toShort()), (1177.toShort()), (1220.toShort()), (1131.toShort()), (1191.toShort()), (963.toShort()), (963.toShort()), ((-1647).toShort()), (961.toShort()), (780.toShort()), ((-1663).toShort()), (558.toShort()), (558.toShort()), (994.toShort()), (993.toShort()), (437.toShort()), (408.toShort()), (393.toShort()), (407.toShort()), (829.toShort()), (978.toShort()), (813.toShort()), (797.toShort()), (947.toShort()), ((-1743).toShort()), (721.toShort()), (721.toShort()), (377.toShort()), (392.toShort()), (844.toShort()), (950.toShort()), (828.toShort()), (890.toShort()), (706.toShort()), (706.toShort()), (812.toShort()), (859.toShort()), (796.toShort()), (960.toShort()), (948.toShort()), (843.toShort()), (934.toShort()), (874.toShort()), (571.toShort()), (571.toShort()), ((-1919).toShort()), (690.toShort()), (555.toShort()), (689.toShort()), (421.toShort()), (346.toShort()), (539.toShort()), (539.toShort()), (944.toShort()), (779.toShort()), (918.toShort()), (873.toShort()), (932.toShort()), (842.toShort()), (903.toShort()), (888.toShort()), (570.toShort()), (570.toShort()), (931.toShort()), (917.toShort()), (674.toShort()), (674.toShort()), ((-2575).toShort()), (1562.toShort()), ((-2591).toShort()), (1609.toShort()), ((-2607).toShort()), (1654.toShort()), (1322.toShort()), (1322.toShort()), (1441.toShort()), (1441.toShort()), (1696.toShort()), (1546.toShort()), (1683.toShort()), (1593.toShort()), (1669.toShort()), (1624.toShort()), (1426.toShort()), (1426.toShort()), (1321.toShort()), (1321.toShort()), (1639.toShort()), (1680.toShort()), (1425.toShort()), (1425.toShort()), (1305.toShort()), (1305.toShort()), (1545.toShort()), (1668.toShort()), (1608.toShort()), (1623.toShort()), (1667.toShort()), (1592.toShort()), (1638.toShort()), (1666.toShort()), (1320.toShort()), (1320.toShort()), (1652.toShort()), (1607.toShort()), (1409.toShort()), (1409.toShort()), (1304.toShort()), (1304.toShort()), (1288.toShort()), (1288.toShort()), (1664.toShort()), (1637.toShort()), (1395.toShort()), (1395.toShort()), (1335.toShort()), (1335.toShort()), (1622.toShort()), (1636.toShort()), (1394.toShort()), (1394.toShort()), (1319.toShort()), (1319.toShort()), (1606.toShort()), (1621.toShort()), (1392.toShort()), (1392.toShort()), (1137.toShort()), (1137.toShort()), (1137.toShort()), (1137.toShort()), (345.toShort()), (390.toShort()), (360.toShort()), (375.toShort()), (404.toShort()), (373.toShort()), (1047.toShort()), ((-2751).toShort()), ((-2767).toShort()), ((-2783).toShort()), (1062.toShort()), (1121.toShort()), (1046.toShort()), ((-2799).toShort()), (1077.toShort()), ((-2815).toShort()), (1106.toShort()), (1061.toShort()), (789.toShort()), (789.toShort()), (1105.toShort()), (1104.toShort()), (263.toShort()), (355.toShort()), (310.toShort()), (340.toShort()), (325.toShort()), (354.toShort()), (352.toShort()), (262.toShort()), (339.toShort()), (324.toShort()), (1091.toShort()), (1076.toShort()), (1029.toShort()), (1090.toShort()), (1060.toShort()), (1075.toShort()), (833.toShort()), (833.toShort()), (788.toShort()), (788.toShort()), (1088.toShort()), (1028.toShort()), (818.toShort()), (818.toShort()), (803.toShort()), (803.toShort()), (561.toShort()), (561.toShort()), (531.toShort()), (531.toShort()), (816.toShort()), (771.toShort()), (546.toShort()), (546.toShort()), (289.toShort()), (274.toShort()), (288.toShort()), (258.toShort()), ((-253).toShort()), ((-317).toShort()), ((-381).toShort()), ((-446).toShort()), ((-478).toShort()), ((-509).toShort()), (1279.toShort()), (1279.toShort()), ((-811).toShort()), ((-1179).toShort()), ((-1451).toShort()), ((-1756).toShort()), ((-1900).toShort()), ((-2028).toShort()), ((-2189).toShort()), ((-2253).toShort()), ((-2333).toShort()), ((-2414).toShort()), ((-2445).toShort()), ((-2511).toShort()), ((-2526).toShort()), (1313.toShort()), (1298.toShort()), ((-2559).toShort()), (1041.toShort()), (1041.toShort()), (1040.toShort()), (1040.toShort()), (1025.toShort()), (1025.toShort()), (1024.toShort()), (1024.toShort()), (1022.toShort()), (1007.toShort()), (1021.toShort()), (991.toShort()), (1020.toShort()), (975.toShort()), (1019.toShort()), (959.toShort()), (687.toShort()), (687.toShort()), (1018.toShort()), (1017.toShort()), (671.toShort()), (671.toShort()), (655.toShort()), (655.toShort()), (1016.toShort()), (1015.toShort()), (639.toShort()), (639.toShort()), (758.toShort()), (758.toShort()), (623.toShort()), (623.toShort()), (757.toShort()), (607.toShort()), (756.toShort()), (591.toShort()), (755.toShort()), (575.toShort()), (754.toShort()), (559.toShort()), (543.toShort()), (543.toShort()), (1009.toShort()), (783.toShort()), ((-575).toShort()), ((-621).toShort()), ((-685).toShort()), ((-749).toShort()), (496.toShort()), ((-590).toShort()), (750.toShort()), (749.toShort()), (734.toShort()), (748.toShort()), (974.toShort()), (989.toShort()), (1003.toShort()), (958.toShort()), (988.toShort()), (973.toShort()), (1002.toShort()), (942.toShort()), (987.toShort()), (957.toShort()), (972.toShort()), (1001.toShort()), (926.toShort()), (986.toShort()), (941.toShort()), (971.toShort()), (956.toShort()), (1000.toShort()), (910.toShort()), (985.toShort()), (925.toShort()), (999.toShort()), (894.toShort()), (970.toShort()), ((-1071).toShort()), ((-1087).toShort()), ((-1102).toShort()), (1390.toShort()), ((-1135).toShort()), (1436.toShort()), (1509.toShort()), (1451.toShort()), (1374.toShort()), ((-1151).toShort()), (1405.toShort()), (1358.toShort()), (1480.toShort()), (1420.toShort()), ((-1167).toShort()), (1507.toShort()), (1494.toShort()), (1389.toShort()), (1342.toShort()), (1465.toShort()), (1435.toShort()), (1450.toShort()), (1326.toShort()), (1505.toShort()), (1310.toShort()), (1493.toShort()), (1373.toShort()), (1479.toShort()), (1404.toShort()), (1492.toShort()), (1464.toShort()), (1419.toShort()), (428.toShort()), (443.toShort()), (472.toShort()), (397.toShort()), (736.toShort()), (526.toShort()), (464.toShort()), (464.toShort()), (486.toShort()), (457.toShort()), (442.toShort()), (471.toShort()), (484.toShort()), (482.toShort()), (1357.toShort()), (1449.toShort()), (1434.toShort()), (1478.toShort()), (1388.toShort()), (1491.toShort()), (1341.toShort()), (1490.toShort()), (1325.toShort()), (1489.toShort()), (1463.toShort()), (1403.toShort()), (1309.toShort()), (1477.toShort()), (1372.toShort()), (1448.toShort()), (1418.toShort()), (1433.toShort()), (1476.toShort()), (1356.toShort()), (1462.toShort()), (1387.toShort()), ((-1439).toShort()), (1475.toShort()), (1340.toShort()), (1447.toShort()), (1402.toShort()), (1474.toShort()), (1324.toShort()), (1461.toShort()), (1371.toShort()), (1473.toShort()), (269.toShort()), (448.toShort()), (1432.toShort()), (1417.toShort()), (1308.toShort()), (1460.toShort()), ((-1711).toShort()), (1459.toShort()), ((-1727).toShort()), (1441.toShort()), (1099.toShort()), (1099.toShort()), (1446.toShort()), (1386.toShort()), (1431.toShort()), (1401.toShort()), ((-1743).toShort()), (1289.toShort()), (1083.toShort()), (1083.toShort()), (1160.toShort()), (1160.toShort()), (1458.toShort()), (1445.toShort()), (1067.toShort()), (1067.toShort()), (1370.toShort()), (1457.toShort()), (1307.toShort()), (1430.toShort()), (1129.toShort()), (1129.toShort()), (1098.toShort()), (1098.toShort()), (268.toShort()), (432.toShort()), (267.toShort()), (416.toShort()), (266.toShort()), (400.toShort()), ((-1887).toShort()), (1144.toShort()), (1187.toShort()), (1082.toShort()), (1173.toShort()), (1113.toShort()), (1186.toShort()), (1066.toShort()), (1050.toShort()), (1158.toShort()), (1128.toShort()), (1143.toShort()), (1172.toShort()), (1097.toShort()), (1171.toShort()), (1081.toShort()), (420.toShort()), (391.toShort()), (1157.toShort()), (1112.toShort()), (1170.toShort()), (1142.toShort()), (1127.toShort()), (1065.toShort()), (1169.toShort()), (1049.toShort()), (1156.toShort()), (1096.toShort()), (1141.toShort()), (1111.toShort()), (1155.toShort()), (1080.toShort()), (1126.toShort()), (1154.toShort()), (1064.toShort()), (1153.toShort()), (1140.toShort()), (1095.toShort()), (1048.toShort()), ((-2159).toShort()), (1125.toShort()), (1110.toShort()), (1137.toShort()), ((-2175).toShort()), (823.toShort()), (823.toShort()), (1139.toShort()), (1138.toShort()), (807.toShort()), (807.toShort()), (384.toShort()), (264.toShort()), (368.toShort()), (263.toShort()), (868.toShort()), (838.toShort()), (853.toShort()), (791.toShort()), (867.toShort()), (822.toShort()), (852.toShort()), (837.toShort()), (866.toShort()), (806.toShort()), (865.toShort()), (790.toShort()), ((-2319).toShort()), (851.toShort()), (821.toShort()), (836.toShort()), (352.toShort()), (262.toShort()), (850.toShort()), (805.toShort()), (849.toShort()), ((-2399).toShort()), (533.toShort()), (533.toShort()), (835.toShort()), (820.toShort()), (336.toShort()), (261.toShort()), (578.toShort()), (548.toShort()), (563.toShort()), (577.toShort()), (532.toShort()), (532.toShort()), (832.toShort()), (772.toShort()), (562.toShort()), (562.toShort()), (547.toShort()), (547.toShort()), (305.toShort()), (275.toShort()), (560.toShort()), (515.toShort()), (290.toShort()), (290.toShort()), (288.toShort()), (258.toShort()))
    var __STATIC_L3_huffman_tabs: CPointer<Short> = __STATIC_L3_huffman_tabs__INIT()
    var __STATIC_L3_huffman_tab32: CPointer<UByte> = { fixedArrayOfUByte(28, (130.toUByte()), (162.toUByte()), (193.toUByte()), (209.toUByte()), (44.toUByte()), (28.toUByte()), (76.toUByte()), (140.toUByte()), (9.toUByte()), (9.toUByte()), (9.toUByte()), (9.toUByte()), (9.toUByte()), (9.toUByte()), (9.toUByte()), (9.toUByte()), (190.toUByte()), (254.toUByte()), (222.toUByte()), (238.toUByte()), (126.toUByte()), (94.toUByte()), (157.toUByte()), (157.toUByte()), (109.toUByte()), (61.toUByte()), (173.toUByte()), (205.toUByte())) }()
    fun __STATIC_L3_huffman_tab33__INIT() = fixedArrayOfUByte(16, (252.toUByte()), (236.toUByte()), (220.toUByte()), (204.toUByte()), (188.toUByte()), (172.toUByte()), (156.toUByte()), (140.toUByte()), (124.toUByte()), (108.toUByte()), (92.toUByte()), (76.toUByte()), (60.toUByte()), (44.toUByte()), (28.toUByte()), (12.toUByte()))
    var __STATIC_L3_huffman_tab33: CPointer<UByte> = __STATIC_L3_huffman_tab33__INIT()
    fun __STATIC_L3_huffman_tabindex__INIT() = Array32ShortAlloc((0.toShort()), (32.toShort()), (64.toShort()), (98.toShort()), (0.toShort()), (132.toShort()), (180.toShort()), (218.toShort()), (292.toShort()), (364.toShort()), (426.toShort()), (538.toShort()), (648.toShort()), (746.toShort()), (0.toShort()), (1126.toShort()), (1460.toShort()), (1460.toShort()), (1460.toShort()), (1460.toShort()), (1460.toShort()), (1460.toShort()), (1460.toShort()), (1460.toShort()), (1842.toShort()), (1842.toShort()), (1842.toShort()), (1842.toShort()), (1842.toShort()), (1842.toShort()), (1842.toShort()), (1842.toShort()))
    var __STATIC_L3_huffman_tabindex: Array32Short = __STATIC_L3_huffman_tabindex__INIT()
    fun __STATIC_L3_huffman_g_linbits__INIT() = fixedArrayOfUByte(32, (0.toUByte()), (0.toUByte()), (0.toUByte()), (0.toUByte()), (0.toUByte()), (0.toUByte()), (0.toUByte()), (0.toUByte()), (0.toUByte()), (0.toUByte()), (0.toUByte()), (0.toUByte()), (0.toUByte()), (0.toUByte()), (0.toUByte()), (0.toUByte()), (1.toUByte()), (2.toUByte()), (3.toUByte()), (4.toUByte()), (6.toUByte()), (8.toUByte()), (10.toUByte()), (13.toUByte()), (4.toUByte()), (5.toUByte()), (6.toUByte()), (7.toUByte()), (8.toUByte()), (9.toUByte()), (11.toUByte()), (13.toUByte()))
    var __STATIC_L3_huffman_g_linbits: CPointer<UByte> = __STATIC_L3_huffman_g_linbits__INIT()
    fun __STATIC_L3_stereo_process_g_pan__INIT() = Array14FloatAlloc((0.toFloat()), (1.toFloat()), 0.21132487f, 0.78867513f, 0.3660254f, 0.6339746f, 0.5f, 0.5f, 0.6339746f, 0.3660254f, 0.78867513f, 0.21132487f, (1.toFloat()), (0.toFloat()))
    var __STATIC_L3_stereo_process_g_pan: Array14Float = __STATIC_L3_stereo_process_g_pan__INIT()
    fun __STATIC_L3_antialias_g_aa__INIT() = Array2Array8FloatAlloc(Array8FloatAlloc(0.85749293f, 0.881742f, 0.94962865f, 0.98331459f, 0.99551782f, 0.99916056f, 0.9998992f, 0.99999316f), Array8FloatAlloc(0.51449576f, 0.47173197f, 0.31337745f, 0.1819132f, 0.09457419f, 0.04096558f, 0.01419856f, 0.00369997f))
    var __STATIC_L3_antialias_g_aa: Array2Array8Float = __STATIC_L3_antialias_g_aa__INIT()
    fun __STATIC_L3_imdct36_g_twid9__INIT() = Array18FloatAlloc(0.73727734f, 0.79335334f, 0.84339145f, 0.88701083f, 0.92387953f, 0.95371695f, 0.97629601f, 0.99144486f, 0.99904822f, 0.67559021f, 0.60876143f, 0.53729961f, 0.46174861f, 0.38268343f, 0.3007058f, 0.21643961f, 0.13052619f, 0.04361938f)
    var __STATIC_L3_imdct36_g_twid9: Array18Float = __STATIC_L3_imdct36_g_twid9__INIT()
    fun __STATIC_L3_imdct12_g_twid3__INIT() = Array6FloatAlloc(0.79335334f, 0.92387953f, 0.99144486f, 0.60876143f, 0.38268343f, 0.13052619f)
    var __STATIC_L3_imdct12_g_twid3: Array6Float = __STATIC_L3_imdct12_g_twid3__INIT()
    fun __STATIC_L3_imdct_gr_g_mdct_window__INIT() = Array2Array18FloatAlloc(Array18FloatAlloc(0.99904822f, 0.99144486f, 0.97629601f, 0.95371695f, 0.92387953f, 0.88701083f, 0.84339145f, 0.79335334f, 0.73727734f, 0.04361938f, 0.13052619f, 0.21643961f, 0.3007058f, 0.38268343f, 0.46174861f, 0.53729961f, 0.60876143f, 0.67559021f), Array18FloatAlloc((1.toFloat()), (1.toFloat()), (1.toFloat()), (1.toFloat()), (1.toFloat()), (1.toFloat()), 0.99144486f, 0.92387953f, 0.79335334f, (0.toFloat()), (0.toFloat()), (0.toFloat()), (0.toFloat()), (0.toFloat()), (0.toFloat()), 0.13052619f, 0.38268343f, 0.60876143f))
    var __STATIC_L3_imdct_gr_g_mdct_window: Array2Array18Float = __STATIC_L3_imdct_gr_g_mdct_window__INIT()
    fun __STATIC_mp3d_DCT_II_g_sec__INIT() = Array24FloatAlloc(10.19000816f, 0.50060302f, 0.50241929f, 3.40760851f, 0.50547093f, 0.52249861f, 2.05778098f, 0.51544732f, 0.56694406f, 1.4841646f, 0.53104258f, 0.6468218f, 1.16943991f, 0.55310392f, 0.7881546f, 0.97256821f, 0.58293498f, 1.06067765f, 0.83934963f, 0.62250412f, 1.72244716f, 0.74453628f, 0.67480832f, 5.10114861f)
    var __STATIC_mp3d_DCT_II_g_sec: Array24Float = __STATIC_mp3d_DCT_II_g_sec__INIT()
    fun __STATIC_mp3d_synth_g_win__INIT() = fixedArrayOfFloat(240, ((-1).toFloat()), (26.toFloat()), ((-31).toFloat()), (208.toFloat()), (218.toFloat()), (401.toFloat()), ((-519).toFloat()), (2063.toFloat()), (2000.toFloat()), (4788.toFloat()), ((-5517).toFloat()), (7134.toFloat()), (5959.toFloat()), (35640.toFloat()), ((-39336).toFloat()), (74992.toFloat()), ((-1).toFloat()), (24.toFloat()), ((-35).toFloat()), (202.toFloat()), (222.toFloat()), (347.toFloat()), ((-581).toFloat()), (2080.toFloat()), (1952.toFloat()), (4425.toFloat()), ((-5879).toFloat()), (7640.toFloat()), (5288.toFloat()), (33791.toFloat()), ((-41176).toFloat()), (74856.toFloat()), ((-1).toFloat()), (21.toFloat()), ((-38).toFloat()), (196.toFloat()), (225.toFloat()), (294.toFloat()), ((-645).toFloat()), (2087.toFloat()), (1893.toFloat()), (4063.toFloat()), ((-6237).toFloat()), (8092.toFloat()), (4561.toFloat()), (31947.toFloat()), ((-43006).toFloat()), (74630.toFloat()), ((-1).toFloat()), (19.toFloat()), ((-41).toFloat()), (190.toFloat()), (227.toFloat()), (244.toFloat()), ((-711).toFloat()), (2085.toFloat()), (1822.toFloat()), (3705.toFloat()), ((-6589).toFloat()), (8492.toFloat()), (3776.toFloat()), (30112.toFloat()), ((-44821).toFloat()), (74313.toFloat()), ((-1).toFloat()), (17.toFloat()), ((-45).toFloat()), (183.toFloat()), (228.toFloat()), (197.toFloat()), ((-779).toFloat()), (2075.toFloat()), (1739.toFloat()), (3351.toFloat()), ((-6935).toFloat()), (8840.toFloat()), (2935.toFloat()), (28289.toFloat()), ((-46617).toFloat()), (73908.toFloat()), ((-1).toFloat()), (16.toFloat()), ((-49).toFloat()), (176.toFloat()), (228.toFloat()), (153.toFloat()), ((-848).toFloat()), (2057.toFloat()), (1644.toFloat()), (3004.toFloat()), ((-7271).toFloat()), (9139.toFloat()), (2037.toFloat()), (26482.toFloat()), ((-48390).toFloat()), (73415.toFloat()), ((-2).toFloat()), (14.toFloat()), ((-53).toFloat()), (169.toFloat()), (227.toFloat()), (111.toFloat()), ((-919).toFloat()), (2032.toFloat()), (1535.toFloat()), (2663.toFloat()), ((-7597).toFloat()), (9389.toFloat()), (1082.toFloat()), (24694.toFloat()), ((-50137).toFloat()), (72835.toFloat()), ((-2).toFloat()), (13.toFloat()), ((-58).toFloat()), (161.toFloat()), (224.toFloat()), (72.toFloat()), ((-991).toFloat()), (2001.toFloat()), (1414.toFloat()), (2330.toFloat()), ((-7910).toFloat()), (9592.toFloat()), (70.toFloat()), (22929.toFloat()), ((-51853).toFloat()), (72169.toFloat()), ((-2).toFloat()), (11.toFloat()), ((-63).toFloat()), (154.toFloat()), (221.toFloat()), (36.toFloat()), ((-1064).toFloat()), (1962.toFloat()), (1280.toFloat()), (2006.toFloat()), ((-8209).toFloat()), (9750.toFloat()), ((-998).toFloat()), (21189.toFloat()), ((-53534).toFloat()), (71420.toFloat()), ((-2).toFloat()), (10.toFloat()), ((-68).toFloat()), (147.toFloat()), (215.toFloat()), (2.toFloat()), ((-1137).toFloat()), (1919.toFloat()), (1131.toFloat()), (1692.toFloat()), ((-8491).toFloat()), (9863.toFloat()), ((-2122).toFloat()), (19478.toFloat()), ((-55178).toFloat()), (70590.toFloat()), ((-3).toFloat()), (9.toFloat()), ((-73).toFloat()), (139.toFloat()), (208.toFloat()), ((-29).toFloat()), ((-1210).toFloat()), (1870.toFloat()), (970.toFloat()), (1388.toFloat()), ((-8755).toFloat()), (9935.toFloat()), ((-3300).toFloat()), (17799.toFloat()), ((-56778).toFloat()), (69679.toFloat()), ((-3).toFloat()), (8.toFloat()), ((-79).toFloat()), (132.toFloat()), (200.toFloat()), ((-57).toFloat()), ((-1283).toFloat()), (1817.toFloat()), (794.toFloat()), (1095.toFloat()), ((-8998).toFloat()), (9966.toFloat()), ((-4533).toFloat()), (16155.toFloat()), ((-58333).toFloat()), (68692.toFloat()), ((-4).toFloat()), (7.toFloat()), ((-85).toFloat()), (125.toFloat()), (189.toFloat()), ((-83).toFloat()), ((-1356).toFloat()), (1759.toFloat()), (605.toFloat()), (814.toFloat()), ((-9219).toFloat()), (9959.toFloat()), ((-5818).toFloat()), (14548.toFloat()), ((-59838).toFloat()), (67629.toFloat()), ((-4).toFloat()), (7.toFloat()), ((-91).toFloat()), (117.toFloat()), (177.toFloat()), ((-106).toFloat()), ((-1428).toFloat()), (1698.toFloat()), (402.toFloat()), (545.toFloat()), ((-9416).toFloat()), (9916.toFloat()), ((-7154).toFloat()), (12980.toFloat()), ((-61289).toFloat()), (66494.toFloat()), ((-5).toFloat()), (6.toFloat()), ((-97).toFloat()), (111.toFloat()), (163.toFloat()), ((-127).toFloat()), ((-1498).toFloat()), (1634.toFloat()), (185.toFloat()), (288.toFloat()), ((-9585).toFloat()), (9838.toFloat()), ((-8540).toFloat()), (11455.toFloat()), ((-62684).toFloat()), (65290.toFloat()))
    var __STATIC_mp3d_synth_g_win: CPointer<Float> = __STATIC_mp3d_synth_g_win__INIT()
    // typealias uint8_t = UByte
    // typealias uint16_t = UShort
    // typealias uint32_t = UInt
    // typealias uint64_t = ULong
    // typealias int8_t = Byte
    // typealias int16_t = Short
    // typealias int32_t = Int
    // typealias int64_t = Long
    // typealias mp3dec_frame_info_t = mp3dec_frame_info_t
    // typealias mp3dec_t = mp3dec_t
    // var mp3dec_init: CFunction1<CPointer<struct null>, Unit> = 0 /*CFunction1<CPointer<mp3dec_t>, Unit>*/
    // typealias mp3d_sample_t = Short
    // var mp3dec_decode_frame: CFunction5<CPointer<struct null>, CPointer<UByte>, Int, CPointer<Short>, CPointer<struct null>, Int> = 0 /*CFunction5<CPointer<mp3dec_t>, CPointer<uint8_t>, Int, CPointer<mp3d_sample_t>, CPointer<mp3dec_frame_info_t>, Int>*/
    // typealias size_t = Int
    // var free: CFunction1<CPointer<Unit>, Unit> = 0 /*CFunction1<CPointer<Unit>, Unit>*/
    // var malloc: CFunction1<Int, CPointer<Unit>> = 0 /*CFunction1<size_t, CPointer<Unit>>*/
    // var realloc: CFunction2<CPointer<Unit>, Int, CPointer<Unit>> = 0 /*CFunction2<CPointer<Unit>, size_t, CPointer<Unit>>*/
    // var exit: CFunction1<Int, Unit> = 0 /*CFunction1<Int, Unit>*/
    // var memset: CFunction3<CPointer<Unit>, Int, Int, CPointer<Unit>> = 0 /*CFunction3<CPointer<Unit>, Int, size_t, CPointer<Unit>>*/
    // var memcpy: CFunction3<CPointer<Unit>, CPointer<Unit>, Int, CPointer<Unit>> = 0 /*CFunction3<CPointer<Unit>, CPointer<Unit>, size_t, CPointer<Unit>>*/
    // var memmove: CFunction3<CPointer<Unit>, CPointer<Unit>, Int, CPointer<Unit>> = 0 /*CFunction3<CPointer<Unit>, CPointer<Unit>, size_t, CPointer<Unit>>*/
    // typealias bs_t = bs_t
    // typealias L12_scale_info = L12_scale_info
    // typealias L12_subband_alloc_t = L12_subband_alloc_t
    // typealias L3_gr_info_t = L3_gr_info_t
    // typealias mp3dec_scratch_t = mp3dec_scratch_t
    fun bs_init(bs: CPointer<bs_t>, data: CPointer<UByte>, bytes: Int): Unit = stackFrame {
        bs.value.buf = data
        bs.value.pos = 0
        bs.value.limit = bytes * 8
    }
    fun get_bits(bs: CPointer<bs_t>, n: Int): UInt = stackFrame {
        var next: UInt = 0u
        var cache: UInt = (0.toUInt())
        var s: UInt = ((bs.value.pos and 7).toUInt())
        var shl: Int = (n + (s.toInt()))
        var p: CPointer<UByte> = (bs.value.buf + (bs.value.pos shr (3).toInt()))
        if ((run { bs.value.pos + n }.also { `$` -> bs.value.pos = `$` }) > bs.value.limit) {
            return 0.toUInt()
        }
        next = (p.also { p += 1 }.value.toUInt()) and ((255 shr ((s.toInt())).toInt()).toUInt())
        while ((run { shl - 8 }.also { `$` -> shl = `$` }) > 0) {
            cache = cache or (next shl (shl).toInt())
            next = p.also { p += 1 }.value.toUInt()
        }
        return cache or (next shr ((-shl)).toInt())
    }
    fun hdr_valid(h: CPointer<UByte>): Int = stackFrame {
        return (((((h[0] == (255.toUByte())) && (((((h[1].toUInt()) and (240.toUInt())).toInt()) == 240) || ((((h[1].toUInt()) and (254.toUInt())).toInt()) == 226))) && (((((h[1].toUInt()) shr (1).toInt()) and (3.toUInt())).toInt()) != 0)) && ((((h[2].toUInt()) shr (4).toInt()).toInt()) != 15)) && (((((h[2].toUInt()) shr (2).toInt()) and (3.toUInt())).toInt()) != 3)).toInt().toInt()
    }
    fun hdr_compare(h1: CPointer<UByte>, h2: CPointer<UByte>): Int = stackFrame {
        return ((((hdr_valid(h2).toBool()) && (((((h1[1].toUInt()) xor (h2[1].toUInt())) and (254.toUInt())).toInt()) == 0)) && (((((h1[2].toUInt()) xor (h2[2].toUInt())) and (12.toUInt())).toInt()) == 0)) && (!(((((((h1[2].toUInt()) and (240.toUInt())).toInt()) == 0).toInt().toInt()) xor (((((h2[2].toUInt()) and (240.toUInt())).toInt()) == 0).toInt().toInt())).toBool()))).toInt().toInt()
    }
    fun hdr_bitrate_kbps(h: CPointer<UByte>): UInt = stackFrame {
        var halfrate: Array2Array3Array15UByte = __STATIC_hdr_bitrate_kbps_halfrate
        return (2 * (halfrate[(!(!(((h[1].toUInt()) and (8.toUInt())).toBool()))).toInt().toInt()][((((h[1].toUInt()) shr (1).toInt()) and (3.toUInt())).toInt()) - 1][((h[2].toUInt()) shr (4).toInt()).toInt()].toInt())).toUInt()
    }
    fun hdr_sample_rate_hz(h: CPointer<UByte>): UInt = stackFrame {
        var g_hz: Array3UInt = __STATIC_hdr_sample_rate_hz_g_hz
        return (g_hz[(((h[2].toUInt()) shr (2).toInt()) and (3.toUInt())).toInt()] shr (((!(((h[1].toUInt()) and (8.toUInt())).toBool())).toInt().toInt())).toInt()) shr (((!(((h[1].toUInt()) and (16.toUInt())).toBool())).toInt().toInt())).toInt()
    }
    fun hdr_frame_samples(h: CPointer<UByte>): UInt = stackFrame {
        return (if ((((h[1].toUInt()) and (6.toUInt())).toInt()) == 6) 384 else (1152 shr ((((((h[1].toUInt()) and (14.toUInt())).toInt()) == 2).toInt().toInt())).toInt())).toUInt()
    }
    fun hdr_frame_bytes(h: CPointer<UByte>, free_format_size: Int): Int = stackFrame {
        var frame_bytes: Int = ((((hdr_frame_samples(h) * hdr_bitrate_kbps(h)) * (125.toUInt())) / hdr_sample_rate_hz(h)).toInt())
        if ((((h[1].toUInt()) and (6.toUInt())).toInt()) == 6) {
            frame_bytes = frame_bytes and ((3).inv())
        }
        return (if (frame_bytes.toBool()) frame_bytes else free_format_size)
    }
    fun hdr_padding(h: CPointer<UByte>): Int = stackFrame {
        return (if (((h[2].toUInt()) and (2.toUInt())).toBool()) (if ((((h[1].toUInt()) and (6.toUInt())).toInt()) == 6) 4 else 1) else 0)
    }
    fun L12_subband_alloc_table(hdr: CPointer<UByte>, sci: CPointer<L12_scale_info>): CPointer<L12_subband_alloc_t> = stackFrame {
        var alloc: CPointer<L12_subband_alloc_t> = CPointer(0)
        var mode: Int = ((((hdr[3].toUInt()) shr (6).toInt()) and (3.toUInt())).toInt())
        var nbands: Int = 0
        var stereo_bands: Int = (if (mode == 3) 0 else (if (mode == 1) ((((((hdr[3].toUInt()) shr (4).toInt()) and (3.toUInt())) shl (2).toInt()).toInt()) + 4) else 32))
        if ((((hdr[1].toUInt()) and (6.toUInt())).toInt()) == 6) {
            stackFrame {
                var g_alloc_L1: CPointer<L12_subband_alloc_t> = __STATIC_L12_subband_alloc_table_g_alloc_L1
                alloc = CPointer<L12_subband_alloc_t>((g_alloc_L1).ptr)
                nbands = 32
            }
        } else {
            if (!(((hdr[1].toUInt()) and (8.toUInt())).toBool())) {
                stackFrame {
                    var g_alloc_L2M2: CPointer<L12_subband_alloc_t> = __STATIC_L12_subband_alloc_table_g_alloc_L2M2
                    alloc = CPointer<L12_subband_alloc_t>((g_alloc_L2M2).ptr)
                    nbands = 30
                }
            } else {
                stackFrame {
                    var g_alloc_L2M1: CPointer<L12_subband_alloc_t> = __STATIC_L12_subband_alloc_table_g_alloc_L2M1
                    var sample_rate_idx: Int = ((((hdr[2].toUInt()) shr (2).toInt()) and (3.toUInt())).toInt())
                    var kbps: UInt = (hdr_bitrate_kbps(hdr) shr (((mode != 3).toInt().toInt())).toInt())
                    if (!(kbps.toBool())) {
                        kbps = 192.toUInt()
                    }
                    alloc = CPointer<L12_subband_alloc_t>((g_alloc_L2M1).ptr)
                    nbands = 27
                    if ((kbps.toInt()) < 56) {
                        stackFrame {
                            var g_alloc_L2M1_lowrate: CPointer<L12_subband_alloc_t> = __STATIC_L12_subband_alloc_table_g_alloc_L2M1_lowrate
                            alloc = CPointer<L12_subband_alloc_t>((g_alloc_L2M1_lowrate).ptr)
                            nbands = (if (sample_rate_idx == 2) 12 else 8)
                        }
                    } else {
                        if (((kbps.toInt()) >= 96) && (sample_rate_idx != 1)) {
                            nbands = 30
                        }
                    }
                }
            }
        }
        sci.value.total_bands = nbands.toUByte()
        sci.value.stereo_bands = (if (stereo_bands > nbands) nbands else stereo_bands).toUByte()
        return alloc
    }
    fun L12_read_scalefactors(bs: CPointer<bs_t>, pba: CPointer<UByte>, scfcod: CPointer<UByte>, bands: Int, scf: CPointer<Float>): Unit = stackFrame {
        var pba = pba // Mutating parameter
        var scf = scf // Mutating parameter
        var g_deq_L12: Array54Float = __STATIC_L12_read_scalefactors_g_deq_L12
        var i: Int = 0
        var m: Int = 0
        i = 0
        while (i < bands) {
            stackFrame {
                var s: Float = (0.toFloat())
                var ba: Int = (pba.also { pba += 1 }.value.toInt())
                var mask: Int = (if (ba.toBool()) (4 + ((19 shr ((scfcod[i].toInt())).toInt()) and 3)) else 0)
                m = 4
                while (m.toBool()) {
                    if ((mask and m).toBool()) {
                        stackFrame {
                            var b: Int = (get_bits(bs, 6).toInt())
                            s = g_deq_L12[((ba * 3) - 6) + (b % 3)] * (((1 shl (21).toInt()) shr ((b / 3)).toInt()).toFloat())
                        }
                    }
                    scf.also { scf += 1 }.value = s
                    m = m shr (1).toInt()
                }
            }
            i += 1
        }
    }
    fun L12_read_scale_info(hdr: CPointer<UByte>, bs: CPointer<bs_t>, sci: CPointer<L12_scale_info>): Unit = stackFrame {
        var g_bitalloc_code_tab: CPointer<UByte> = __STATIC_L12_read_scale_info_g_bitalloc_code_tab
        var subband_alloc: CPointer<L12_subband_alloc_t> = L12_subband_alloc_table(hdr, sci)
        var i: Int = 0
        var k: Int = 0
        var ba_bits: Int = 0
        var ba_code_tab: CPointer<UByte> = (CPointer<UByte>((g_bitalloc_code_tab).ptr))
        i = 0
        while (i < (sci.value.total_bands.toInt())) {
            stackFrame {
                var ba: UByte = 0u
                if (i == k) {
                    k = k + (subband_alloc.value.band_count.toInt())
                    ba_bits = subband_alloc.value.code_tab_width.toInt()
                    ba_code_tab = CPointer<UByte>(((g_bitalloc_code_tab + (subband_alloc.value.tab_offset.toInt()))).ptr)
                    subband_alloc += 1
                }
                ba = ba_code_tab[get_bits(bs, ba_bits).toInt()]
                sci.value.bitalloc[(2 * i)] = ba
                if (i < (sci.value.stereo_bands.toInt())) {
                    ba = ba_code_tab[get_bits(bs, ba_bits).toInt()]
                }
                sci.value.bitalloc[((2 * i) + 1)] = (if (sci.value.stereo_bands.toBool()) ba else (0.toUByte()))
            }
            i += 1
        }
        i = 0
        while (i < (2 * (sci.value.total_bands.toInt()))) {
            sci.value.scfcod[i] = (if (sci.value.bitalloc[i].toBool()) (if ((((hdr[1].toUInt()) and (6.toUInt())).toInt()) == 6) 2 else (get_bits(bs, 2).toInt())) else 6).toUByte()
            i += 1
        }
        L12_read_scalefactors(bs, (CPointer<UByte>((sci.value.bitalloc).ptr)), (CPointer<UByte>((sci.value.scfcod).ptr)), (((sci.value.total_bands.toUInt()) * (2.toUInt())).toInt()), (CPointer<Float>((sci.value.scf).ptr)))
        i = sci.value.stereo_bands.toInt()
        while (i < (sci.value.total_bands.toInt())) {
            sci.value.bitalloc[((2 * i) + 1)] = 0.toUByte()
            i += 1
        }
    }
    fun L12_dequantize_granule(grbuf: CPointer<Float>, bs: CPointer<bs_t>, sci: CPointer<L12_scale_info>, group_size: Int): Int = stackFrame {
        var i: Int = 0
        var j: Int = 0
        var k: Int = 0
        var choff: Int = 576
        j = 0
        while (j < 4) {
            stackFrame {
                var dst: CPointer<Float> = (grbuf + (group_size * j))
                i = 0
                while (i < (2 * (sci.value.total_bands.toInt()))) {
                    stackFrame {
                        var ba: Int = (sci.value.bitalloc[i].toInt())
                        if (ba != 0) {
                            if (ba < 17) {
                                stackFrame {
                                    var half: Int = ((1 shl ((ba - 1)).toInt()) - 1)
                                    k = 0
                                    while (k < group_size) {
                                        dst[k] = ((get_bits(bs, ba).toInt()) - half).toFloat()
                                        k += 1
                                    }
                                }
                            } else {
                                stackFrame {
                                    var mod: UInt = (((2 shl ((ba - 17)).toInt()) + 1).toUInt())
                                    var code: UInt = get_bits(bs, (((mod.toInt()) + 2) - ((mod shr (3).toInt()).toInt())))
                                    k = 0
                                    while (k < group_size) {
                                        dst[k] = (((code % mod) - (mod / (2.toUInt()))).toInt()).toFloat()
                                        run { k++; run { code / mod }.also { `$` -> code = `$` } }
                                    }
                                }
                            }
                        }
                        dst = dst + choff
                        choff = 18 - choff
                    }
                    i += 1
                }
            }
            j += 1
        }
        return group_size * 4
    }
    fun L12_apply_scf_384(sci: CPointer<L12_scale_info>, scf: CPointer<Float>, dst: CPointer<Float>): Unit = stackFrame {
        var scf = scf // Mutating parameter
        var dst = dst // Mutating parameter
        var i: Int = 0
        var k: Int = 0
        memcpy((CPointer<Unit>((((dst + 576) + (((sci.value.stereo_bands.toUInt()) * (18.toUInt())).toInt()))).ptr)), (CPointer<Unit>(((dst + (((sci.value.stereo_bands.toUInt()) * (18.toUInt())).toInt()))).ptr)), (((((sci.value.total_bands.toUInt()) - (sci.value.stereo_bands.toUInt())) * (18.toUInt())) * (Float.SIZE_BYTES.toUInt())).toInt()))
        i = 0
        while (i < (sci.value.total_bands.toInt())) {
            k = 0
            while (k < 12) {
                dst[(k + 0)] = dst[k + 0] * scf[0]
                dst[(k + 576)] = dst[k + 576] * scf[3]
                k += 1
            }
            run { i++; run { dst + 18 }.also { `$` -> dst = `$` }; run { scf + 6 }.also { `$` -> scf = `$` } }
        }
    }
    fun L3_read_side_info(bs: CPointer<bs_t>, gr: CPointer<L3_gr_info_t>, hdr: CPointer<UByte>): Int = stackFrame {
        var gr = gr // Mutating parameter
        var g_scf_long: Array8Array23UByte = __STATIC_L3_read_side_info_g_scf_long
        var g_scf_short: Array8Array40UByte = __STATIC_L3_read_side_info_g_scf_short
        var g_scf_mixed: Array8Array40UByte = __STATIC_L3_read_side_info_g_scf_mixed
        var tables: UInt = 0u
        var scfsi: UInt = (0.toUInt())
        var main_data_begin: Int = 0
        var part_23_sum: Int = 0
        var sr_idx: Int = (((((hdr[2].toUInt()) shr (2).toInt()) and (3.toUInt())) + (((((hdr[1].toUInt()) shr (3).toInt()) and (1.toUInt())) + (((hdr[1].toUInt()) shr (4).toInt()) and (1.toUInt()))) * (3.toUInt()))).toInt())
        sr_idx = sr_idx - ((sr_idx != 0).toInt().toInt())
        var gr_count: Int = (if ((((hdr[3].toUInt()) and (192.toUInt())).toInt()) == 192) 1 else 2)
        if (((hdr[1].toUInt()) and (8.toUInt())).toBool()) {
            gr_count = gr_count * 2
            main_data_begin = get_bits(bs, 9).toInt()
            scfsi = get_bits(bs, (7 + gr_count))
        } else {
            main_data_begin = (get_bits(bs, (8 + gr_count)) shr (gr_count).toInt()).toInt()
        }
        do0@do {
            if ((((hdr[3].toUInt()) and (192.toUInt())).toInt()) == 192) {
                scfsi = scfsi shl (4).toInt()
            }
            gr.value.part_23_length = get_bits(bs, 12).toUShort()
            part_23_sum = part_23_sum + (gr.value.part_23_length.toInt())
            gr.value.big_values = get_bits(bs, 9).toUShort()
            if (gr.value.big_values > (288.toUShort())) {
                return -1
            }
            gr.value.global_gain = get_bits(bs, 8).toUByte()
            gr.value.scalefac_compress = get_bits(bs, (if (((hdr[1].toUInt()) and (8.toUInt())).toBool()) 4 else 9)).toUShort()
            gr.value.sfbtab = CPointer<UByte>((g_scf_long[sr_idx]).ptr)
            gr.value.n_long_sfb = 22.toUByte()
            gr.value.n_short_sfb = 0.toUByte()
            if (get_bits(bs, 1).toBool()) {
                gr.value.block_type = get_bits(bs, 2).toUByte()
                if (!(gr.value.block_type.toBool())) {
                    return -1
                }
                gr.value.mixed_block_flag = get_bits(bs, 1).toUByte()
                gr.value.region_count[0] = 7.toUByte()
                gr.value.region_count[1] = 255.toUByte()
                if (gr.value.block_type == (2.toUByte())) {
                    scfsi = scfsi and (3855.toUInt())
                    if (!(gr.value.mixed_block_flag.toBool())) {
                        gr.value.region_count[0] = 8.toUByte()
                        gr.value.sfbtab = CPointer<UByte>((g_scf_short[sr_idx]).ptr)
                        gr.value.n_long_sfb = 0.toUByte()
                        gr.value.n_short_sfb = 39.toUByte()
                    } else {
                        gr.value.sfbtab = CPointer<UByte>((g_scf_mixed[sr_idx]).ptr)
                        gr.value.n_long_sfb = (if (((hdr[1].toUInt()) and (8.toUInt())).toBool()) 8 else 6).toUByte()
                        gr.value.n_short_sfb = 30.toUByte()
                    }
                }
                tables = get_bits(bs, 10)
                tables = tables shl (5).toInt()
                gr.value.subblock_gain[0] = get_bits(bs, 3).toUByte()
                gr.value.subblock_gain[1] = get_bits(bs, 3).toUByte()
                gr.value.subblock_gain[2] = get_bits(bs, 3).toUByte()
            } else {
                gr.value.block_type = 0.toUByte()
                gr.value.mixed_block_flag = 0.toUByte()
                tables = get_bits(bs, 15)
                gr.value.region_count[0] = get_bits(bs, 4).toUByte()
                gr.value.region_count[1] = get_bits(bs, 3).toUByte()
                gr.value.region_count[2] = 255.toUByte()
            }
            gr.value.table_select[0] = (tables shr (10).toInt()).toUByte()
            gr.value.table_select[1] = ((tables shr (5).toInt()) and (31.toUInt())).toUByte()
            gr.value.table_select[2] = (tables and (31.toUInt())).toUByte()
            gr.value.preflag = (if (((hdr[1].toUInt()) and (8.toUInt())).toBool()) get_bits(bs, 1) else ((gr.value.scalefac_compress >= (500.toUShort())).toInt().toUInt())).toUByte()
            gr.value.scalefac_scale = get_bits(bs, 1).toUByte()
            gr.value.count1_table = get_bits(bs, 1).toUByte()
            gr.value.scfsi = ((scfsi shr (12).toInt()) and (15.toUInt())).toUByte()
            scfsi = scfsi shl (4).toInt()
            gr += 1
        } while ((--gr_count).toBool())
        if ((part_23_sum + bs.value.pos) > (bs.value.limit + (main_data_begin * 8))) {
            return -1
        }
        return main_data_begin
    }
    fun L3_read_scalefactors(scf: CPointer<UByte>, ist_pos: CPointer<UByte>, scf_size: CPointer<UByte>, scf_count: CPointer<UByte>, bitbuf: CPointer<bs_t>, scfsi: Int): Unit = stackFrame {
        var scf = scf // Mutating parameter
        var ist_pos = ist_pos // Mutating parameter
        var scfsi = scfsi // Mutating parameter
        var i: Int = 0
        var k: Int = 0
        i = 0
        while ((i < 4) && (scf_count[i].toBool())) {
            stackFrame {
                var cnt: Int = (scf_count[i].toInt())
                if ((scfsi and 8).toBool()) {
                    memcpy((CPointer<Unit>((scf).ptr)), (CPointer<Unit>((ist_pos).ptr)), cnt)
                } else {
                    stackFrame {
                        var bits: Int = (scf_size[i].toInt())
                        if (!(bits.toBool())) {
                            memset((CPointer<Unit>((scf).ptr)), 0, cnt)
                            memset((CPointer<Unit>((ist_pos).ptr)), 0, cnt)
                        } else {
                            stackFrame {
                                var max_scf: Int = (if (scfsi < 0) ((1 shl (bits).toInt()) - 1) else (-1))
                                k = 0
                                while (k < cnt) {
                                    stackFrame {
                                        var s: Int = (get_bits(bitbuf, bits).toInt())
                                        ist_pos[k] = (if (s == max_scf) (-1) else s).toUByte()
                                        scf[k] = s.toUByte()
                                    }
                                    k += 1
                                }
                            }
                        }
                    }
                }
                ist_pos = ist_pos + cnt
                scf = scf + cnt
            }
            run { i++; run { scfsi * 2 }.also { `$` -> scfsi = `$` } }
        }
        scf[0] = run { run { 0.toUByte() }.also { `$` -> scf[2] = `$` } }.also { `$` -> scf[1] = `$` }
    }
    fun L3_ldexp_q2(y: Float, exp_q2: Int): Float = stackFrame {
        var y = y // Mutating parameter
        var exp_q2 = exp_q2 // Mutating parameter
        var g_expfrac: Array4Float = __STATIC_L3_ldexp_q2_g_expfrac
        var e: Int = 0
        do0@do {
            e = (if ((30 * 4) > exp_q2) exp_q2 else (30 * 4))
            y = y * (g_expfrac[e and 3] * (((1 shl (30).toInt()) shr ((e shr (2).toInt())).toInt()).toFloat()))
        } while ((run { exp_q2 - e }.also { `$` -> exp_q2 = `$` }) > 0)
        return y
    }
    fun L3_decode_scalefactors(hdr: CPointer<UByte>, ist_pos: CPointer<UByte>, bs: CPointer<bs_t>, gr: CPointer<L3_gr_info_t>, scf: CPointer<Float>, ch: Int): Unit = stackFrame {
        var g_scf_partitions: Array3Array28UByte = __STATIC_L3_decode_scalefactors_g_scf_partitions
        var scf_partition: CPointer<UByte> = (CPointer<UByte>((g_scf_partitions[((!(!(gr.value.n_short_sfb.toBool()))).toInt().toInt()) + ((!(gr.value.n_long_sfb.toBool())).toInt().toInt())]).ptr))
        var scf_size: Array4UByte = Array4UByteAlloc((0.toUByte()))
        var iscf: Array40UByte = Array40UByteAlloc((0.toUByte()))
        var i: Int = 0
        var scf_shift: Int = (((gr.value.scalefac_scale.toUInt()) + (1.toUInt())).toInt())
        var gain_exp: Int = 0
        var scfsi: Int = (gr.value.scfsi.toInt())
        var gain: Float = 0f
        if (((hdr[1].toUInt()) and (8.toUInt())).toBool()) {
            stackFrame {
                var g_scfc_decode: Array16UByte = __STATIC_L3_decode_scalefactors_g_scfc_decode
                var part: Int = (g_scfc_decode[gr.value.scalefac_compress.toInt()].toInt())
                scf_size[1] = run { (part shr (2).toInt()).toUByte() }.also { `$` -> scf_size[0] = `$` }
                scf_size[3] = run { (part and 3).toUByte() }.also { `$` -> scf_size[2] = `$` }
            }
        } else {
            stackFrame {
                var g_mod: Array24UByte = __STATIC_L3_decode_scalefactors_g_mod
                var k: Int = 0
                var modprod: Int = 0
                var sfc: Int = 0
                var ist: Int = (((((hdr[3].toUInt()) and (16.toUInt())).toBool()) && (ch.toBool())).toInt().toInt())
                sfc = ((gr.value.scalefac_compress.toUInt()) shr (ist).toInt()).toInt()
                k = (ist * 3) * 4
                while (sfc >= 0) {
                    run { run { 1 }.also { `$` -> modprod = `$` }; run { 3 }.also { `$` -> i = `$` } }
                    while (i >= 0) {
                        scf_size[i] = ((sfc / modprod) % (g_mod[k + i].toInt())).toUByte()
                        modprod = modprod * (g_mod[k + i].toInt())
                        i -= 1
                    }
                    run { run { sfc - modprod }.also { `$` -> sfc = `$` }; run { k + 4 }.also { `$` -> k = `$` } }
                }
                scf_partition = scf_partition + k
                scfsi = -16
            }
        }
        L3_read_scalefactors((CPointer<UByte>((iscf).ptr)), ist_pos, (CPointer<UByte>((scf_size).ptr)), scf_partition, bs, scfsi)
        if (gr.value.n_short_sfb.toBool()) {
            stackFrame {
                var sh: Int = (3 - scf_shift)
                i = 0
                while (i < (gr.value.n_short_sfb.toInt())) {
                    iscf[((((gr.value.n_long_sfb.toUInt()) + (i.toUInt())).toInt()) + 0)] = ((iscf[(((gr.value.n_long_sfb.toUInt()) + (i.toUInt())).toInt()) + 0].toUInt()) + ((gr.value.subblock_gain[0].toUInt()) shl (sh).toInt())).toUByte()
                    iscf[((((gr.value.n_long_sfb.toUInt()) + (i.toUInt())).toInt()) + 1)] = ((iscf[(((gr.value.n_long_sfb.toUInt()) + (i.toUInt())).toInt()) + 1].toUInt()) + ((gr.value.subblock_gain[1].toUInt()) shl (sh).toInt())).toUByte()
                    iscf[((((gr.value.n_long_sfb.toUInt()) + (i.toUInt())).toInt()) + 2)] = ((iscf[(((gr.value.n_long_sfb.toUInt()) + (i.toUInt())).toInt()) + 2].toUInt()) + ((gr.value.subblock_gain[2].toUInt()) shl (sh).toInt())).toUByte()
                    i = i + 3
                }
            }
        } else {
            if (gr.value.preflag.toBool()) {
                stackFrame {
                    var g_preamp: Array10UByte = __STATIC_L3_decode_scalefactors_g_preamp
                    i = 0
                    while (i < 10) {
                        iscf[(11 + i)] = ((iscf[11 + i].toUInt()) + (g_preamp[i].toUInt())).toUByte()
                        i += 1
                    }
                }
            }
        }
        gain_exp = ((((gr.value.global_gain.toUInt()) + (((-1) * 4).toUInt())).toInt()) - 210) - (if ((((hdr[3].toUInt()) and (224.toUInt())).toInt()) == 96) 2 else 0)
        gain = L3_ldexp_q2(((1 shl ((((((255 + ((-1) * 4)) - 210) + 3) and ((3).inv())) / 4)).toInt()).toFloat()), (((((255 + ((-1) * 4)) - 210) + 3) and ((3).inv())) - gain_exp))
        i = 0
        while (i < (((gr.value.n_long_sfb.toUInt()) + (gr.value.n_short_sfb.toUInt())).toInt())) {
            scf[i] = L3_ldexp_q2(gain, (((iscf[i].toUInt()) shl (scf_shift).toInt()).toInt()))
            i += 1
        }
    }
    var g_pow43: Array145Float /*static*/ = Array145FloatAlloc((0.toFloat()), ((-1).toFloat()), (-2.519842f), (-4.326749f), (-6.349604f), (-8.54988f), (-10.902724f), (-13.390518f), (-16f), (-18.720754f), (-21.544347f), (-24.463781f), (-27.473142f), (-30.567351f), (-33.741992f), (-36.993181f), (0.toFloat()), (1.toFloat()), 2.519842f, 4.326749f, 6.349604f, 8.54988f, 10.902724f, 13.390518f, 16f, 18.720754f, 21.544347f, 24.463781f, 27.473142f, 30.567351f, 33.741992f, 36.993181f, 40.317474f, 43.711787f, 47.173345f, 50.699631f, 54.288352f, 57.937408f, 61.644865f, 65.408941f, 69.227979f, 73.100443f, 77.024898f, 81f, 85.024491f, 89.097188f, 93.216975f, 97.3828f, 101.593667f, 105.848633f, 110.146801f, 114.487321f, 118.869381f, 123.292209f, 127.755065f, 132.257246f, 136.798076f, 141.376907f, 145.993119f, 150.646117f, 155.335327f, 160.060199f, 164.820202f, 169.614826f, 174.443577f, 179.30598f, 184.201575f, 189.129918f, 194.09058f, 199.083145f, 204.10721f, 209.162385f, 214.248292f, 219.364564f, 224.510845f, 229.686789f, 234.892058f, 240.126328f, 245.38928f, 250.680604f, 256f, 261.347174f, 266.721841f, 272.123723f, 277.552547f, 283.008049f, 288.489971f, 293.99806f, 299.532071f, 305.091761f, 310.676898f, 316.287249f, 321.922592f, 327.582707f, 333.267377f, 338.976394f, 344.70955f, 350.466646f, 356.247482f, 362.051866f, 367.879608f, 373.730522f, 379.604427f, 385.501143f, 391.420496f, 397.362314f, 403.326427f, 409.312672f, 415.320884f, 421.350905f, 427.402579f, 433.47575f, 439.570269f, 445.685987f, 451.822757f, 457.980436f, 464.158883f, 470.35796f, 476.57753f, 482.817459f, 489.077615f, 495.357868f, 501.65809f, 507.978156f, 514.317941f, 520.677324f, 527.056184f, 533.454404f, 539.871867f, 546.308458f, 552.764065f, 559.238575f, 565.731879f, 572.24387f, 578.77444f, 585.323483f, 591.890898f, 598.476581f, 605.080431f, 611.702349f, 618.342238f, 625f, 631.67554f, 638.368763f, 645.079578f)
    fun L3_pow_43(x: Int): Float = stackFrame {
        var x = x // Mutating parameter
        var frac: Float = 0f
        var sign: Int = 0
        var mult: Int = 256
        if (x < 129) {
            return g_pow43[16 + x]
        }
        if (x < 1024) {
            mult = 16
            x = x shl (3).toInt()
        }
        sign = (2 * x) and 64
        frac = (((x and 63) - sign).toFloat()) / (((x and ((63).inv())) + sign).toFloat())
        return (g_pow43[16 + ((x + sign) shr (6).toInt())] * (1f + (frac * ((4f / (3.toFloat())) + (frac * (2f / (9.toFloat()))))))) * (mult.toFloat())
    }
    fun L3_huffman(dst: CPointer<Float>, bs: CPointer<bs_t>, gr_info: CPointer<L3_gr_info_t>, scf: CPointer<Float>, layer3gr_limit: Int): Unit = stackFrame {
        var dst = dst // Mutating parameter
        var scf = scf // Mutating parameter
        var tabs: CPointer<Short> = __STATIC_L3_huffman_tabs
        var tab32: CPointer<UByte> = __STATIC_L3_huffman_tab32
        var tab33: CPointer<UByte> = __STATIC_L3_huffman_tab33
        var tabindex: Array32Short = __STATIC_L3_huffman_tabindex
        var g_linbits: CPointer<UByte> = __STATIC_L3_huffman_g_linbits
        var one: Float = 0f
        var ireg: Int = 0
        var big_val_cnt: Int = (gr_info.value.big_values.toInt())
        var sfb: CPointer<UByte> = gr_info.value.sfbtab
        var bs_next_ptr: CPointer<UByte> = (bs.value.buf + (bs.value.pos / 8))
        var bs_cache: UInt = ((((((((bs_next_ptr[0].toUInt()) * (256.toUInt())) + (bs_next_ptr[1].toUInt())) * (256.toUInt())) + (bs_next_ptr[2].toUInt())) * (256.toUInt())) + (bs_next_ptr[3].toUInt())) shl ((bs.value.pos and 7)).toInt())
        var pairs_to_decode: Int = 0
        var np: Int = 0
        var bs_sh: Int = ((bs.value.pos and 7) - 8)
        bs_next_ptr = bs_next_ptr + 4
        while (big_val_cnt > 0) {
            stackFrame {
                var tab_num: Int = (gr_info.value.table_select[ireg].toInt())
                var sfb_cnt: Int = (gr_info.value.region_count[ireg++].toInt())
                var codebook: CPointer<Short> = (CPointer<Short>(((tabs + (tabindex[tab_num].toInt()))).ptr))
                var linbits: Int = (g_linbits[tab_num].toInt())
                if (linbits.toBool()) {
                    do0@do {
                        np = ((sfb.also { sfb += 1 }.value.toUInt()) / (2.toUInt())).toInt()
                        pairs_to_decode = (if (big_val_cnt > np) np else big_val_cnt)
                        one = scf.also { scf += 1 }.value
                        do1@do {
                            stackFrame {
                                var j: Int = 0
                                var w: Int = 5
                                var leaf: Int = (codebook[(bs_cache shr ((32 - w)).toInt()).toInt()].toInt())
                                while (leaf < 0) {
                                    bs_cache = bs_cache shl (w).toInt()
                                    bs_sh = bs_sh + w
                                    w = leaf and 7
                                    leaf = codebook[((bs_cache shr ((32 - w)).toInt()).toInt()) - (leaf shr (3).toInt())].toInt()
                                }
                                bs_cache = bs_cache shl ((leaf shr (8).toInt())).toInt()
                                bs_sh = bs_sh + (leaf shr (8).toInt())
                                j = 0
                                while (j < 2) {
                                    stackFrame {
                                        var lsb: Int = (leaf and 15)
                                        if (lsb == 15) {
                                            lsb = lsb + ((bs_cache shr ((32 - linbits)).toInt()).toInt())
                                            bs_cache = bs_cache shl (linbits).toInt()
                                            bs_sh = bs_sh + linbits
                                            while (bs_sh >= 0) {
                                                bs_cache = bs_cache or ((bs_next_ptr.also { bs_next_ptr += 1 }.value.toUInt()) shl (bs_sh).toInt())
                                                bs_sh = bs_sh - 8
                                            }
                                            dst.value = (one * L3_pow_43(lsb)) * ((if ((bs_cache.toInt()) < 0) (-1) else 1).toFloat())
                                        } else {
                                            dst.value = g_pow43[(16 + lsb) - (16 * ((bs_cache shr (31).toInt()).toInt()))] * one
                                        }
                                        bs_cache = bs_cache shl ((if (lsb.toBool()) 1 else 0)).toInt()
                                        bs_sh = bs_sh + (if (lsb.toBool()) 1 else 0)
                                    }
                                    run { j++; dst.also { dst += 1 }; run { leaf shr (4).toInt() }.also { `$` -> leaf = `$` } }
                                }
                                while (bs_sh >= 0) {
                                    bs_cache = bs_cache or ((bs_next_ptr.also { bs_next_ptr += 1 }.value.toUInt()) shl (bs_sh).toInt())
                                    bs_sh = bs_sh - 8
                                }
                            }
                        } while ((--pairs_to_decode).toBool())
                    } while (((run { big_val_cnt - np }.also { `$` -> big_val_cnt = `$` }) > 0) && ((--sfb_cnt) >= 0))
                } else {
                    do0@do {
                        np = ((sfb.also { sfb += 1 }.value.toUInt()) / (2.toUInt())).toInt()
                        pairs_to_decode = (if (big_val_cnt > np) np else big_val_cnt)
                        one = scf.also { scf += 1 }.value
                        do1@do {
                            stackFrame {
                                var j: Int = 0
                                var w: Int = 5
                                var leaf: Int = (codebook[(bs_cache shr ((32 - w)).toInt()).toInt()].toInt())
                                while (leaf < 0) {
                                    bs_cache = bs_cache shl (w).toInt()
                                    bs_sh = bs_sh + w
                                    w = leaf and 7
                                    leaf = codebook[((bs_cache shr ((32 - w)).toInt()).toInt()) - (leaf shr (3).toInt())].toInt()
                                }
                                bs_cache = bs_cache shl ((leaf shr (8).toInt())).toInt()
                                bs_sh = bs_sh + (leaf shr (8).toInt())
                                j = 0
                                while (j < 2) {
                                    stackFrame {
                                        var lsb: Int = (leaf and 15)
                                        dst.value = g_pow43[(16 + lsb) - (16 * ((bs_cache shr (31).toInt()).toInt()))] * one
                                        bs_cache = bs_cache shl ((if (lsb.toBool()) 1 else 0)).toInt()
                                        bs_sh = bs_sh + (if (lsb.toBool()) 1 else 0)
                                    }
                                    run { j++; dst.also { dst += 1 }; run { leaf shr (4).toInt() }.also { `$` -> leaf = `$` } }
                                }
                                while (bs_sh >= 0) {
                                    bs_cache = bs_cache or ((bs_next_ptr.also { bs_next_ptr += 1 }.value.toUInt()) shl (bs_sh).toInt())
                                    bs_sh = bs_sh - 8
                                }
                            }
                        } while ((--pairs_to_decode).toBool())
                    } while (((run { big_val_cnt - np }.also { `$` -> big_val_cnt = `$` }) > 0) && ((--sfb_cnt) >= 0))
                }
            }
        }
        np = 1 - big_val_cnt
        while0@while (1.toBool()) {
            val __oldPos0 = STACK_PTR
            try {
                var codebook_count1: CPointer<UByte> = (CPointer<UByte>(((if (gr_info.value.count1_table.toBool()) tab33 else tab32)).ptr))
                var leaf: Int = (codebook_count1[(bs_cache shr ((32 - 4)).toInt()).toInt()].toInt())
                if (!((leaf and 8).toBool())) {
                    leaf = codebook_count1[(leaf shr (3).toInt()) + (((bs_cache shl (4).toInt()) shr ((32 - (leaf and 3))).toInt()).toInt())].toInt()
                }
                bs_cache = bs_cache shl ((leaf and 7)).toInt()
                bs_sh = bs_sh + (leaf and 7)
                if (((((bs_next_ptr - bs.value.buf) * 8) - 24) + bs_sh) > layer3gr_limit) {
                    break@while0
                }
                if (!((--np).toBool())) {
                    np = ((sfb.also { sfb += 1 }.value.toUInt()) / (2.toUInt())).toInt()
                    if (!(np.toBool())) {
                        break@while0
                    }
                    one = scf.also { scf += 1 }.value
                }
                if ((leaf and (128 shr (0).toInt())).toBool()) {
                    dst[0] = (if ((bs_cache.toInt()) < 0) (-one) else one)
                    bs_cache = bs_cache shl (1).toInt()
                    bs_sh = bs_sh + 1
                }
                if ((leaf and (128 shr (1).toInt())).toBool()) {
                    dst[1] = (if ((bs_cache.toInt()) < 0) (-one) else one)
                    bs_cache = bs_cache shl (1).toInt()
                    bs_sh = bs_sh + 1
                }
                if (!((--np).toBool())) {
                    np = ((sfb.also { sfb += 1 }.value.toUInt()) / (2.toUInt())).toInt()
                    if (!(np.toBool())) {
                        break@while0
                    }
                    one = scf.also { scf += 1 }.value
                }
                if ((leaf and (128 shr (2).toInt())).toBool()) {
                    dst[2] = (if ((bs_cache.toInt()) < 0) (-one) else one)
                    bs_cache = bs_cache shl (1).toInt()
                    bs_sh = bs_sh + 1
                }
                if ((leaf and (128 shr (3).toInt())).toBool()) {
                    dst[3] = (if ((bs_cache.toInt()) < 0) (-one) else one)
                    bs_cache = bs_cache shl (1).toInt()
                    bs_sh = bs_sh + 1
                }
                while (bs_sh >= 0) {
                    bs_cache = bs_cache or ((bs_next_ptr.also { bs_next_ptr += 1 }.value.toUInt()) shl (bs_sh).toInt())
                    bs_sh = bs_sh - 8
                }
            }
            finally {
                STACK_PTR = __oldPos0
            }
            dst = dst + 4
        }
        bs.value.pos = layer3gr_limit
    }
    fun L3_midside_stereo(left: CPointer<Float>, n: Int): Unit = stackFrame {
        var i: Int = 0
        var right: CPointer<Float> = (left + 576)
        while (i < n) {
            stackFrame {
                var a: Float = left[i]
                var b: Float = right[i]
                left[i] = a + b
                right[i] = a - b
            }
            i += 1
        }
    }
    fun L3_intensity_stereo_band(left: CPointer<Float>, n: Int, kl: Float, kr: Float): Unit = stackFrame {
        var i: Int = 0
        i = 0
        while (i < n) {
            left[(i + 576)] = left[i] * kr
            left[i] = left[i] * kl
            i += 1
        }
    }
    fun L3_stereo_top_band(right: CPointer<Float>, sfb: CPointer<UByte>, nbands: Int, max_band: Array3Int): Unit = stackFrame {
        var right = right // Mutating parameter
        var i: Int = 0
        var k: Int = 0
        max_band[0] = run { run { -1 }.also { `$` -> max_band[2] = `$` } }.also { `$` -> max_band[1] = `$` }
        i = 0
        while0@while (i < nbands) {
            k = 0
            while1@while (k < (sfb[i].toInt())) {
                if ((right[k] != (0.toFloat())) || (right[k + 1] != (0.toFloat()))) {
                    max_band[(i % 3)] = i
                    break@while1
                }
                k = k + 2
            }
            right = right + (sfb[i].toInt())
            i += 1
        }
    }
    fun L3_stereo_process(left: CPointer<Float>, ist_pos: CPointer<UByte>, sfb: CPointer<UByte>, hdr: CPointer<UByte>, max_band: Array3Int, mpeg2_sh: Int): Unit = stackFrame {
        var left = left // Mutating parameter
        var g_pan: Array14Float = __STATIC_L3_stereo_process_g_pan
        var i: UInt = 0u
        var max_pos: UInt = ((if (((hdr[1].toUInt()) and (8.toUInt())).toBool()) 7 else 64).toUInt())
        i = 0.toUInt()
        while (sfb[i.toInt()].toBool()) {
            stackFrame {
                var ipos: UInt = (ist_pos[i.toInt()].toUInt())
                if (((i.toInt()) > max_band[(i % (3.toUInt())).toInt()]) && (ipos < max_pos)) {
                    stackFrame {
                        var kl: Float = 0f
                        var kr: Float = 0f
                        var s: Float = (if (((hdr[3].toUInt()) and (32.toUInt())).toBool()) 1.41421356f else (1.toFloat()))
                        if (((hdr[1].toUInt()) and (8.toUInt())).toBool()) {
                            kl = g_pan[2 * (ipos.toInt())]
                            kr = g_pan[(2 * (ipos.toInt())) + 1]
                        } else {
                            kl = 1.toFloat()
                            kr = L3_ldexp_q2((1.toFloat()), ((((ipos.toInt()) + 1) shr (1).toInt()) shl (mpeg2_sh).toInt()))
                            if ((ipos and (1.toUInt())).toBool()) {
                                kl = kr
                                kr = 1.toFloat()
                            }
                        }
                        L3_intensity_stereo_band(left, (sfb[i.toInt()].toInt()), (kl * s), (kr * s))
                    }
                } else {
                    if (((hdr[3].toUInt()) and (32.toUInt())).toBool()) {
                        L3_midside_stereo(left, (sfb[i.toInt()].toInt()))
                    }
                }
                left = left + (sfb[i.toInt()].toInt())
            }
            i += 1u
        }
    }
    fun L3_intensity_stereo(left: CPointer<Float>, ist_pos: CPointer<UByte>, gr: CPointer<L3_gr_info_t>, hdr: CPointer<UByte>): Unit = stackFrame {
        var max_band: Array3Int = Array3IntAlloc(0)
        var n_sfb: Int = (((gr.value.n_long_sfb.toUInt()) + (gr.value.n_short_sfb.toUInt())).toInt())
        var i: Int = 0
        var max_blocks: Int = (if (gr.value.n_short_sfb.toBool()) 3 else 1)
        L3_stereo_top_band((left + 576), gr.value.sfbtab, n_sfb, max_band)
        if (gr.value.n_long_sfb.toBool()) {
            max_band[0] = run { run { (if ((if (max_band[0] < max_band[1]) max_band[1] else max_band[0]) < max_band[2]) max_band[2] else (if (max_band[0] < max_band[1]) max_band[1] else max_band[0])) }.also { `$` -> max_band[2] = `$` } }.also { `$` -> max_band[1] = `$` }
        }
        i = 0
        while (i < max_blocks) {
            stackFrame {
                var default_pos: Int = (if (((hdr[1].toUInt()) and (8.toUInt())).toBool()) 3 else 0)
                var itop: Int = ((n_sfb - max_blocks) + i)
                var prev: Int = (itop - max_blocks)
                ist_pos[itop] = (if (max_band[i] >= prev) default_pos else (ist_pos[prev].toInt())).toUByte()
            }
            i += 1
        }
        L3_stereo_process(left, ist_pos, gr.value.sfbtab, hdr, max_band, (((gr[1].scalefac_compress.toUInt()) and (1.toUInt())).toInt()))
    }
    fun L3_reorder(grbuf: CPointer<Float>, scratch: CPointer<Float>, sfb: CPointer<UByte>): Unit = stackFrame {
        var sfb = sfb // Mutating parameter
        var i: Int = 0
        var len: Int = 0
        var src: CPointer<Float> = grbuf
        var dst: CPointer<Float> = scratch
        while (0 != (run { sfb.value.toInt() }.also { `$` -> len = `$` })) {
            i = 0
            while (i < len) {
                dst.also { dst += 1 }.value = src[0 * len]
                dst.also { dst += 1 }.value = src[1 * len]
                dst.also { dst += 1 }.value = src[2 * len]
                run { i++; src.also { src += 1 } }
            }
            run { run { sfb + 3 }.also { `$` -> sfb = `$` }; run { src + (2 * len) }.also { `$` -> src = `$` } }
        }
        memcpy((CPointer<Unit>((grbuf).ptr)), (CPointer<Unit>((scratch).ptr)), ((dst - scratch) * Float.SIZE_BYTES))
    }
    fun L3_antialias(grbuf: CPointer<Float>, nbands: Int): Unit = stackFrame {
        var grbuf = grbuf // Mutating parameter
        var nbands = nbands // Mutating parameter
        var g_aa: Array2Array8Float = __STATIC_L3_antialias_g_aa
        while (nbands > 0) {
            stackFrame {
                var i: Int = 0
                while (i < 8) {
                    stackFrame {
                        var u: Float = grbuf[18 + i]
                        var d: Float = grbuf[17 - i]
                        grbuf[(18 + i)] = (u * g_aa[0][i]) - (d * g_aa[1][i])
                        grbuf[(17 - i)] = (u * g_aa[1][i]) + (d * g_aa[0][i])
                    }
                    i += 1
                }
            }
            run { nbands--; run { grbuf + 18 }.also { `$` -> grbuf = `$` } }
        }
    }
    fun L3_dct3_9(y: CPointer<Float>): Unit = stackFrame {
        var s0: Float = 0f
        var s1: Float = 0f
        var s2: Float = 0f
        var s3: Float = 0f
        var s4: Float = 0f
        var s5: Float = 0f
        var s6: Float = 0f
        var s7: Float = 0f
        var s8: Float = 0f
        var t0: Float = 0f
        var t2: Float = 0f
        var t4: Float = 0f
        s0 = y[0]
        s2 = y[2]
        s4 = y[4]
        s6 = y[6]
        s8 = y[8]
        t0 = s0 + (s6 * 0.5f)
        s0 = s0 - s6
        t4 = (s4 + s2) * 0.93969262f
        t2 = (s8 + s2) * 0.76604444f
        s6 = (s4 - s8) * 0.17364818f
        s4 = s4 + (s8 - s2)
        s2 = s0 - (s4 * 0.5f)
        y[4] = s4 + s0
        s8 = (t0 - t2) + s6
        s0 = (t0 - t4) + t2
        s4 = (t0 + t4) - s6
        s1 = y[1]
        s3 = y[3]
        s5 = y[5]
        s7 = y[7]
        s3 = s3 * 0.8660254f
        t0 = (s5 + s1) * 0.98480775f
        t4 = (s5 - s7) * 0.34202014f
        t2 = (s1 + s7) * 0.64278761f
        s1 = ((s1 - s5) - s7) * 0.8660254f
        s5 = (t0 - s3) - t2
        s7 = (t4 - s3) - t0
        s3 = (t4 + s3) - t2
        y[0] = s4 - s7
        y[1] = s2 + s1
        y[2] = s0 - s3
        y[3] = s8 + s5
        y[5] = s8 - s5
        y[6] = s0 + s3
        y[7] = s2 - s1
        y[8] = s4 + s7
    }
    fun L3_imdct36(grbuf: CPointer<Float>, overlap: CPointer<Float>, window: CPointer<Float>, nbands: Int): Unit = stackFrame {
        var grbuf = grbuf // Mutating parameter
        var overlap = overlap // Mutating parameter
        var i: Int = 0
        var j: Int = 0
        var g_twid9: Array18Float = __STATIC_L3_imdct36_g_twid9
        j = 0
        while (j < nbands) {
            stackFrame {
                var co: Array9Float = Array9FloatAlloc((0.toFloat()))
                var si: Array9Float = Array9FloatAlloc((0.toFloat()))
                co[0] = -grbuf[0]
                si[0] = grbuf[17]
                i = 0
                while (i < 4) {
                    si[(8 - (2 * i))] = grbuf[(4 * i) + 1] - grbuf[(4 * i) + 2]
                    co[(1 + (2 * i))] = grbuf[(4 * i) + 1] + grbuf[(4 * i) + 2]
                    si[(7 - (2 * i))] = grbuf[(4 * i) + 4] - grbuf[(4 * i) + 3]
                    co[(2 + (2 * i))] = -(grbuf[(4 * i) + 3] + grbuf[(4 * i) + 4])
                    i += 1
                }
                L3_dct3_9((CPointer<Float>((co).ptr)))
                L3_dct3_9((CPointer<Float>((si).ptr)))
                si[1] = -si[1]
                si[3] = -si[3]
                si[5] = -si[5]
                si[7] = -si[7]
                i = 0
                while (i < 9) {
                    stackFrame {
                        var ovl: Float = overlap[i]
                        var sum: Float = ((co[i] * g_twid9[9 + i]) + (si[i] * g_twid9[0 + i]))
                        overlap[i] = (co[i] * g_twid9[0 + i]) - (si[i] * g_twid9[9 + i])
                        grbuf[i] = (ovl * window[0 + i]) - (sum * window[9 + i])
                        grbuf[(17 - i)] = (ovl * window[9 + i]) + (sum * window[0 + i])
                    }
                    i += 1
                }
            }
            run { j++; run { grbuf + 18 }.also { `$` -> grbuf = `$` }; run { overlap + 9 }.also { `$` -> overlap = `$` } }
        }
    }
    fun L3_idct3(x0: Float, x1: Float, x2: Float, dst: CPointer<Float>): Unit = stackFrame {
        var m1: Float = (x1 * 0.8660254f)
        var a1: Float = (x0 - (x2 * 0.5f))
        dst[1] = x0 + x2
        dst[0] = a1 + m1
        dst[2] = a1 - m1
    }
    fun L3_imdct12(x: CPointer<Float>, dst: CPointer<Float>, overlap: CPointer<Float>): Unit = stackFrame {
        var g_twid3: Array6Float = __STATIC_L3_imdct12_g_twid3
        var co: Array3Float = Array3FloatAlloc((0.toFloat()))
        var si: Array3Float = Array3FloatAlloc((0.toFloat()))
        var i: Int = 0
        L3_idct3((-x[0]), (x[6] + x[3]), (x[12] + x[9]), (CPointer<Float>((co).ptr)))
        L3_idct3(x[15], (x[12] - x[9]), (x[6] - x[3]), (CPointer<Float>((si).ptr)))
        si[1] = -si[1]
        i = 0
        while (i < 3) {
            stackFrame {
                var ovl: Float = overlap[i]
                var sum: Float = ((co[i] * g_twid3[3 + i]) + (si[i] * g_twid3[0 + i]))
                overlap[i] = (co[i] * g_twid3[0 + i]) - (si[i] * g_twid3[3 + i])
                dst[i] = (ovl * g_twid3[2 - i]) - (sum * g_twid3[5 - i])
                dst[(5 - i)] = (ovl * g_twid3[5 - i]) + (sum * g_twid3[2 - i])
            }
            i += 1
        }
    }
    fun L3_imdct_short(grbuf: CPointer<Float>, overlap: CPointer<Float>, nbands: Int): Unit = stackFrame {
        var grbuf = grbuf // Mutating parameter
        var overlap = overlap // Mutating parameter
        var nbands = nbands // Mutating parameter
        while (nbands > 0) {
            stackFrame {
                var tmp: Array18Float = Array18FloatAlloc((0.toFloat()))
                memcpy((CPointer<Unit>((tmp).ptr)), (CPointer<Unit>((grbuf).ptr)), 72)
                memcpy((CPointer<Unit>((grbuf).ptr)), (CPointer<Unit>((overlap).ptr)), (6 * Float.SIZE_BYTES))
                L3_imdct12((CPointer<Float>((tmp).ptr)), (grbuf + 6), (overlap + 6))
                L3_imdct12((tmp + 1), (grbuf + 12), (overlap + 6))
                L3_imdct12((tmp + 2), overlap, (overlap + 6))
            }
            run { nbands--; run { overlap + 9 }.also { `$` -> overlap = `$` }; run { grbuf + 18 }.also { `$` -> grbuf = `$` } }
        }
    }
    fun L3_change_sign(grbuf: CPointer<Float>): Unit = stackFrame {
        var grbuf = grbuf // Mutating parameter
        var b: Int = 0
        var i: Int = 0
        run { run { 0 }.also { `$` -> b = `$` }; run { grbuf + 18 }.also { `$` -> grbuf = `$` } }
        while (b < 32) {
            i = 1
            while (i < 18) {
                grbuf[i] = -grbuf[i]
                i = i + 2
            }
            run { run { b + 2 }.also { `$` -> b = `$` }; run { grbuf + 36 }.also { `$` -> grbuf = `$` } }
        }
    }
    fun L3_imdct_gr(grbuf: CPointer<Float>, overlap: CPointer<Float>, block_type: UInt, n_long_bands: UInt): Unit = stackFrame {
        var grbuf = grbuf // Mutating parameter
        var overlap = overlap // Mutating parameter
        var g_mdct_window: Array2Array18Float = __STATIC_L3_imdct_gr_g_mdct_window
        if (n_long_bands.toBool()) {
            L3_imdct36(grbuf, overlap, (CPointer<Float>((g_mdct_window[0]).ptr)), (n_long_bands.toInt()))
            grbuf = grbuf + (18 * (n_long_bands.toInt()))
            overlap = overlap + (9 * (n_long_bands.toInt()))
        }
        if ((block_type.toInt()) == 2) {
            L3_imdct_short(grbuf, overlap, (32 - (n_long_bands.toInt())))
        } else {
            L3_imdct36(grbuf, overlap, (CPointer<Float>((g_mdct_window[((block_type.toInt()) == 3).toInt().toInt()]).ptr)), (32 - (n_long_bands.toInt())))
        }
    }
    fun L3_save_reservoir(h: CPointer<mp3dec_t>, s: CPointer<mp3dec_scratch_t>): Unit = stackFrame {
        var pos: Int = ((s.value.bs.pos + 7) / 8)
        var remains: Int = ((s.value.bs.limit / 8) - pos)
        if (remains > 511) {
            pos = pos + (remains - 511)
            remains = 511
        }
        if (remains > 0) {
            memmove((CPointer<Unit>((h.value.reserv_buf).ptr)), (CPointer<Unit>(((s.value.maindata + pos)).ptr)), remains)
        }
        h.value.reserv = remains
    }
    fun L3_restore_reservoir(h: CPointer<mp3dec_t>, bs: CPointer<bs_t>, s: CPointer<mp3dec_scratch_t>, main_data_begin: Int): Int = stackFrame {
        var frame_bytes: Int = ((bs.value.limit - bs.value.pos) / 8)
        var bytes_have: Int = (if (h.value.reserv > main_data_begin) main_data_begin else h.value.reserv)
        memcpy((CPointer<Unit>((s.value.maindata).ptr)), (CPointer<Unit>(((h.value.reserv_buf + (if (0 < (h.value.reserv - main_data_begin)) (h.value.reserv - main_data_begin) else 0))).ptr)), (if (h.value.reserv > main_data_begin) main_data_begin else h.value.reserv))
        memcpy((CPointer<Unit>(((s.value.maindata + bytes_have)).ptr)), (CPointer<Unit>(((bs.value.buf + (bs.value.pos / 8))).ptr)), frame_bytes)
        bs_init((CPointer((s).ptr + mp3dec_scratch_t.OFFSET_bs)), (CPointer<UByte>((s.value.maindata).ptr)), (bytes_have + frame_bytes))
        return (h.value.reserv >= main_data_begin).toInt().toInt()
    }
    fun L3_decode(h: CPointer<mp3dec_t>, s: CPointer<mp3dec_scratch_t>, gr_info: CPointer<L3_gr_info_t>, nch: Int): Unit = stackFrame {
        var gr_info = gr_info // Mutating parameter
        var ch: Int = 0
        ch = 0
        while (ch < nch) {
            stackFrame {
                var layer3gr_limit: Int = (s.value.bs.pos + (gr_info[ch].part_23_length.toInt()))
                L3_decode_scalefactors((CPointer<UByte>((h.value.header).ptr)), (CPointer<UByte>((s.value.ist_pos[ch]).ptr)), (CPointer((s).ptr + mp3dec_scratch_t.OFFSET_bs)), (gr_info + ch), (CPointer<Float>((s.value.scf).ptr)), ch)
                L3_huffman((CPointer<Float>((s.value.grbuf[ch]).ptr)), (CPointer((s).ptr + mp3dec_scratch_t.OFFSET_bs)), (gr_info + ch), (CPointer<Float>((s.value.scf).ptr)), layer3gr_limit)
            }
            ch += 1
        }
        if (((h.value.header[3].toUInt()) and (16.toUInt())).toBool()) {
            L3_intensity_stereo((CPointer<Float>((s.value.grbuf[0]).ptr)), (CPointer<UByte>((s.value.ist_pos[1]).ptr)), gr_info, (CPointer<UByte>((h.value.header).ptr)))
        } else {
            if ((((h.value.header[3].toUInt()) and (224.toUInt())).toInt()) == 96) {
                L3_midside_stereo((CPointer<Float>((s.value.grbuf[0]).ptr)), 576)
            }
        }
        ch = 0
        while (ch < nch) {
            stackFrame {
                var aa_bands: Int = 31
                var n_long_bands: Int = ((if (gr_info.value.mixed_block_flag.toBool()) 2 else 0) shl ((((((((h.value.header[2].toUInt()) shr (2).toInt()) and (3.toUInt())) + (((((h.value.header[1].toUInt()) shr (3).toInt()) and (1.toUInt())) + (((h.value.header[1].toUInt()) shr (4).toInt()) and (1.toUInt()))) * (3.toUInt()))).toInt()) == 2).toInt().toInt())).toInt())
                if (gr_info.value.n_short_sfb.toBool()) {
                    aa_bands = n_long_bands - 1
                    L3_reorder((s.value.grbuf[ch] + (n_long_bands * 18)), (CPointer<Float>((s.value.syn[0]).ptr)), (gr_info.value.sfbtab + (gr_info.value.n_long_sfb.toInt())))
                }
                L3_antialias((CPointer<Float>((s.value.grbuf[ch]).ptr)), aa_bands)
                L3_imdct_gr((CPointer<Float>((s.value.grbuf[ch]).ptr)), (CPointer<Float>((h.value.mdct_overlap[ch]).ptr)), (gr_info.value.block_type.toUInt()), (n_long_bands.toUInt()))
                L3_change_sign((CPointer<Float>((s.value.grbuf[ch]).ptr)))
            }
            run { ch++; gr_info.also { gr_info += 1 } }
        }
    }
    fun mp3d_DCT_II(grbuf: CPointer<Float>, n: Int): Unit = stackFrame {
        var g_sec: Array24Float = __STATIC_mp3d_DCT_II_g_sec
        var i: Int = 0
        var k: Int = 0
        while (k < n) {
            stackFrame {
                var t: Array4Array8Float = Array4Array8FloatAlloc((Array8Float(0)))
                var x: CPointer<Float> = CPointer(0)
                var y: CPointer<Float> = (grbuf + k)
                run { run { CPointer<Float>((t[0]).ptr) }.also { `$` -> x = `$` }; run { 0 }.also { `$` -> i = `$` } }
                while (i < 8) {
                    stackFrame {
                        var x0: Float = y[i * 18]
                        var x1: Float = y[(15 - i) * 18]
                        var x2: Float = y[(16 + i) * 18]
                        var x3: Float = y[(31 - i) * 18]
                        var t0: Float = (x0 + x3)
                        var t1: Float = (x1 + x2)
                        var t2: Float = ((x1 - x2) * g_sec[(3 * i) + 0])
                        var t3: Float = ((x0 - x3) * g_sec[(3 * i) + 1])
                        x[0] = t0 + t1
                        x[8] = (t0 - t1) * g_sec[(3 * i) + 2]
                        x[16] = t3 + t2
                        x[24] = (t3 - t2) * g_sec[(3 * i) + 2]
                    }
                    run { i++; x.also { x += 1 } }
                }
                run { run { CPointer<Float>((t[0]).ptr) }.also { `$` -> x = `$` }; run { 0 }.also { `$` -> i = `$` } }
                while (i < 4) {
                    stackFrame {
                        var x0: Float = x[0]
                        var x1: Float = x[1]
                        var x2: Float = x[2]
                        var x3: Float = x[3]
                        var x4: Float = x[4]
                        var x5: Float = x[5]
                        var x6: Float = x[6]
                        var x7: Float = x[7]
                        var xt: Float = 0f
                        xt = x0 - x7
                        x0 = x0 + x7
                        x7 = x1 - x6
                        x1 = x1 + x6
                        x6 = x2 - x5
                        x2 = x2 + x5
                        x5 = x3 - x4
                        x3 = x3 + x4
                        x4 = x0 - x3
                        x0 = x0 + x3
                        x3 = x1 - x2
                        x1 = x1 + x2
                        x[0] = x0 + x1
                        x[4] = (x0 - x1) * 0.70710677f
                        x5 = x5 + x6
                        x6 = (x6 + x7) * 0.70710677f
                        x7 = x7 + xt
                        x3 = (x3 + x4) * 0.70710677f
                        x5 = x5 - (x7 * 0.198912367f)
                        x7 = x7 + (x5 * 0.382683432f)
                        x5 = x5 - (x7 * 0.198912367f)
                        x0 = xt - x6
                        xt = xt + x6
                        x[1] = (xt + x7) * 0.50979561f
                        x[2] = (x4 + x3) * 0.54119611f
                        x[3] = (x0 - x5) * 0.60134488f
                        x[5] = (x0 + x5) * 0.89997619f
                        x[6] = (x4 - x3) * 1.30656302f
                        x[7] = (xt - x7) * 2.56291556f
                    }
                    run { i++; run { x + 8 }.also { `$` -> x = `$` } }
                }
                i = 0
                while (i < 7) {
                    y[(0 * 18)] = t[0][i]
                    y[(1 * 18)] = (t[2][i] + t[3][i]) + t[3][i + 1]
                    y[(2 * 18)] = t[1][i] + t[1][i + 1]
                    y[(3 * 18)] = (t[2][i + 1] + t[3][i]) + t[3][i + 1]
                    run { i++; run { y + (4 * 18) }.also { `$` -> y = `$` } }
                }
                y[(0 * 18)] = t[0][7]
                y[(1 * 18)] = t[2][7] + t[3][7]
                y[(2 * 18)] = t[1][7]
                y[(3 * 18)] = t[3][7]
            }
            k += 1
        }
    }
    fun mp3d_scale_pcm(sample: Float): Short = stackFrame {
        if ((sample.toDouble()) >= 32766.5) {
            return 32767.toShort()
        }
        if ((sample.toDouble()) <= (-32767.5)) {
            return (-32768).toShort()
        }
        var s: Short = ((sample + 0.5f).toShort())
        s = ((s.toInt()) - ((s < (0.toShort())).toInt().toInt())).toShort()
        return s
    }
    fun mp3d_synth_pair(pcm: CPointer<Short>, nch: Int, z: CPointer<Float>): Unit = stackFrame {
        var z = z // Mutating parameter
        var a: Float = 0f
        a = (z[14 * 64] - z[0]) * (29.toFloat())
        a = a + ((z[1 * 64] + z[13 * 64]) * (213.toFloat()))
        a = a + ((z[12 * 64] - z[2 * 64]) * (459.toFloat()))
        a = a + ((z[3 * 64] + z[11 * 64]) * (2037.toFloat()))
        a = a + ((z[10 * 64] - z[4 * 64]) * (5153.toFloat()))
        a = a + ((z[5 * 64] + z[9 * 64]) * (6574.toFloat()))
        a = a + ((z[8 * 64] - z[6 * 64]) * (37489.toFloat()))
        a = a + (z[7 * 64] * (75038.toFloat()))
        pcm[0] = mp3d_scale_pcm(a)
        z = z + 2
        a = z[14 * 64] * (104.toFloat())
        a = a + (z[12 * 64] * (1567.toFloat()))
        a = a + (z[10 * 64] * (9727.toFloat()))
        a = a + (z[8 * 64] * (64019.toFloat()))
        a = a + (z[6 * 64] * ((-9975).toFloat()))
        a = a + (z[4 * 64] * ((-45).toFloat()))
        a = a + (z[2 * 64] * (146.toFloat()))
        a = a + (z[0 * 64] * ((-5).toFloat()))
        pcm[(16 * nch)] = mp3d_scale_pcm(a)
    }
    fun mp3d_synth(xl: CPointer<Float>, dstl: CPointer<Short>, nch: Int, lins: CPointer<Float>): Unit = stackFrame {
        var i: Int = 0
        var xr: CPointer<Float> = (xl + (576 * (nch - 1)))
        var dstr: CPointer<Short> = (dstl + (nch - 1))
        var g_win: CPointer<Float> = __STATIC_mp3d_synth_g_win
        var zlin: CPointer<Float> = (lins + (15 * 64))
        var w: CPointer<Float> = (CPointer<Float>((g_win).ptr))
        zlin[(4 * 15)] = xl[18 * 16]
        zlin[((4 * 15) + 1)] = xr[18 * 16]
        zlin[((4 * 15) + 2)] = xl[0]
        zlin[((4 * 15) + 3)] = xr[0]
        zlin[(4 * 31)] = xl[1 + (18 * 16)]
        zlin[((4 * 31) + 1)] = xr[1 + (18 * 16)]
        zlin[((4 * 31) + 2)] = xl[1]
        zlin[((4 * 31) + 3)] = xr[1]
        mp3d_synth_pair(dstr, nch, ((lins + (4 * 15)) + 1))
        mp3d_synth_pair((dstr + (32 * nch)), nch, (((lins + (4 * 15)) + 64) + 1))
        mp3d_synth_pair(dstl, nch, (lins + (4 * 15)))
        mp3d_synth_pair((dstl + (32 * nch)), nch, ((lins + (4 * 15)) + 64))
        i = 14
        while (i >= 0) {
            stackFrame {
                var a: Array4Float = Array4FloatAlloc((0.toFloat()))
                var b: Array4Float = Array4FloatAlloc((0.toFloat()))
                zlin[(4 * i)] = xl[18 * (31 - i)]
                zlin[((4 * i) + 1)] = xr[18 * (31 - i)]
                zlin[((4 * i) + 2)] = xl[1 + (18 * (31 - i))]
                zlin[((4 * i) + 3)] = xr[1 + (18 * (31 - i))]
                zlin[(4 * (i + 16))] = xl[1 + (18 * (1 + i))]
                zlin[((4 * (i + 16)) + 1)] = xr[1 + (18 * (1 + i))]
                zlin[((4 * (i - 16)) + 2)] = xl[18 * (1 + i)]
                zlin[((4 * (i - 16)) + 3)] = xr[18 * (1 + i)]
                stackFrame {
                    var j: Int = 0
                    var w0: Float = w.also { w += 1 }.value
                    var w1: Float = w.also { w += 1 }.value
                    var vz: CPointer<Float> = (((zlin) + ((4 * i) - (0 * 64))))
                    var vy: CPointer<Float> = (((zlin) + ((4 * i) - ((15 - 0) * 64))))
                    j = 0
                    while (j < 4) {
                        run { run { (vz[j] * w1) + (vy[j] * w0) }.also { `$` -> b[j] = `$` }; run { (vz[j] * w0) - (vy[j] * w1) }.also { `$` -> a[j] = `$` } }
                        j += 1
                    }
                }
                stackFrame {
                    var j: Int = 0
                    var w0: Float = w.also { w += 1 }.value
                    var w1: Float = w.also { w += 1 }.value
                    var vz: CPointer<Float> = (((zlin) + ((4 * i) - (1 * 64))))
                    var vy: CPointer<Float> = (((zlin) + ((4 * i) - ((15 - 1) * 64))))
                    j = 0
                    while (j < 4) {
                        run { run { b[j] + ((vz[j] * w1) + (vy[j] * w0)) }.also { `$` -> b[j] = `$` }; run { a[j] + ((vy[j] * w1) - (vz[j] * w0)) }.also { `$` -> a[j] = `$` } }
                        j += 1
                    }
                }
                stackFrame {
                    var j: Int = 0
                    var w0: Float = w.also { w += 1 }.value
                    var w1: Float = w.also { w += 1 }.value
                    var vz: CPointer<Float> = (((zlin) + ((4 * i) - (2 * 64))))
                    var vy: CPointer<Float> = (((zlin) + ((4 * i) - ((15 - 2) * 64))))
                    j = 0
                    while (j < 4) {
                        run { run { b[j] + ((vz[j] * w1) + (vy[j] * w0)) }.also { `$` -> b[j] = `$` }; run { a[j] + ((vz[j] * w0) - (vy[j] * w1)) }.also { `$` -> a[j] = `$` } }
                        j += 1
                    }
                }
                stackFrame {
                    var j: Int = 0
                    var w0: Float = w.also { w += 1 }.value
                    var w1: Float = w.also { w += 1 }.value
                    var vz: CPointer<Float> = (((zlin) + ((4 * i) - (3 * 64))))
                    var vy: CPointer<Float> = (((zlin) + ((4 * i) - ((15 - 3) * 64))))
                    j = 0
                    while (j < 4) {
                        run { run { b[j] + ((vz[j] * w1) + (vy[j] * w0)) }.also { `$` -> b[j] = `$` }; run { a[j] + ((vy[j] * w1) - (vz[j] * w0)) }.also { `$` -> a[j] = `$` } }
                        j += 1
                    }
                }
                stackFrame {
                    var j: Int = 0
                    var w0: Float = w.also { w += 1 }.value
                    var w1: Float = w.also { w += 1 }.value
                    var vz: CPointer<Float> = (((zlin) + ((4 * i) - (4 * 64))))
                    var vy: CPointer<Float> = (((zlin) + ((4 * i) - ((15 - 4) * 64))))
                    j = 0
                    while (j < 4) {
                        run { run { b[j] + ((vz[j] * w1) + (vy[j] * w0)) }.also { `$` -> b[j] = `$` }; run { a[j] + ((vz[j] * w0) - (vy[j] * w1)) }.also { `$` -> a[j] = `$` } }
                        j += 1
                    }
                }
                stackFrame {
                    var j: Int = 0
                    var w0: Float = w.also { w += 1 }.value
                    var w1: Float = w.also { w += 1 }.value
                    var vz: CPointer<Float> = (((zlin) + ((4 * i) - (5 * 64))))
                    var vy: CPointer<Float> = (((zlin) + ((4 * i) - ((15 - 5) * 64))))
                    j = 0
                    while (j < 4) {
                        run { run { b[j] + ((vz[j] * w1) + (vy[j] * w0)) }.also { `$` -> b[j] = `$` }; run { a[j] + ((vy[j] * w1) - (vz[j] * w0)) }.also { `$` -> a[j] = `$` } }
                        j += 1
                    }
                }
                stackFrame {
                    var j: Int = 0
                    var w0: Float = w.also { w += 1 }.value
                    var w1: Float = w.also { w += 1 }.value
                    var vz: CPointer<Float> = (((zlin) + ((4 * i) - (6 * 64))))
                    var vy: CPointer<Float> = (((zlin) + ((4 * i) - ((15 - 6) * 64))))
                    j = 0
                    while (j < 4) {
                        run { run { b[j] + ((vz[j] * w1) + (vy[j] * w0)) }.also { `$` -> b[j] = `$` }; run { a[j] + ((vz[j] * w0) - (vy[j] * w1)) }.also { `$` -> a[j] = `$` } }
                        j += 1
                    }
                }
                stackFrame {
                    var j: Int = 0
                    var w0: Float = w.also { w += 1 }.value
                    var w1: Float = w.also { w += 1 }.value
                    var vz: CPointer<Float> = (((zlin) + ((4 * i) - (7 * 64))))
                    var vy: CPointer<Float> = (((zlin) + ((4 * i) - ((15 - 7) * 64))))
                    j = 0
                    while (j < 4) {
                        run { run { b[j] + ((vz[j] * w1) + (vy[j] * w0)) }.also { `$` -> b[j] = `$` }; run { a[j] + ((vy[j] * w1) - (vz[j] * w0)) }.also { `$` -> a[j] = `$` } }
                        j += 1
                    }
                }
                dstr[((15 - i) * nch)] = mp3d_scale_pcm(a[1])
                dstr[((17 + i) * nch)] = mp3d_scale_pcm(b[1])
                dstl[((15 - i) * nch)] = mp3d_scale_pcm(a[0])
                dstl[((17 + i) * nch)] = mp3d_scale_pcm(b[0])
                dstr[((47 - i) * nch)] = mp3d_scale_pcm(a[3])
                dstr[((49 + i) * nch)] = mp3d_scale_pcm(b[3])
                dstl[((47 - i) * nch)] = mp3d_scale_pcm(a[2])
                dstl[((49 + i) * nch)] = mp3d_scale_pcm(b[2])
            }
            i -= 1
        }
    }
    fun mp3d_synth_granule(qmf_state: CPointer<Float>, grbuf: CPointer<Float>, nbands: Int, nch: Int, pcm: CPointer<Short>, lins: CPointer<Float>): Unit = stackFrame {
        var i: Int = 0
        i = 0
        while (i < nch) {
            mp3d_DCT_II((grbuf + (576 * i)), nbands)
            i += 1
        }
        memcpy((CPointer<Unit>((lins).ptr)), (CPointer<Unit>((qmf_state).ptr)), ((Float.SIZE_BYTES * 15) * 64))
        i = 0
        while (i < nbands) {
            mp3d_synth((grbuf + i), (pcm + (32 * (nch * i))), nch, (lins + (i * 64)))
            i = i + 2
        }
        if (nch == 1) {
            i = 0
            while (i < (15 * 64)) {
                qmf_state[i] = lins[(nbands * 64) + i]
                i = i + 2
            }
        } else {
            memcpy((CPointer<Unit>((qmf_state).ptr)), (CPointer<Unit>(((lins + (nbands * 64))).ptr)), ((Float.SIZE_BYTES * 15) * 64))
        }
    }
    fun mp3d_match_frame(hdr: CPointer<UByte>, mp3_bytes: Int, frame_bytes: Int): Int = stackFrame {
        var i: Int = 0
        var nmatch: Int = 0
        run { run { 0 }.also { `$` -> i = `$` }; run { 0 }.also { `$` -> nmatch = `$` } }
        while (nmatch < 10) {
            i = i + (hdr_frame_bytes((hdr + i), frame_bytes) + hdr_padding((hdr + i)))
            if ((i + 4) > mp3_bytes) {
                return (nmatch > 0).toInt().toInt()
            }
            if (!(hdr_compare(hdr, (hdr + i)).toBool())) {
                return 0
            }
            nmatch += 1
        }
        return 1
    }
    fun mp3d_find_frame(mp3: CPointer<UByte>, mp3_bytes: Int, free_format_bytes: CPointer<Int>, ptr_frame_bytes: CPointer<Int>): Int = stackFrame {
        var mp3 = mp3 // Mutating parameter
        var i: Int = 0
        var k: Int = 0
        i = 0
        while0@while (i < (mp3_bytes - 4)) {
            if (hdr_valid(mp3).toBool()) {
                val __oldPos1 = STACK_PTR
                try {
                    var frame_bytes: Int = hdr_frame_bytes(mp3, free_format_bytes.value)
                    var frame_and_padding: Int = (frame_bytes + hdr_padding(mp3))
                    k = 4
                    while1@while (((!(frame_bytes.toBool())) && (k < 2304)) && ((i + (2 * ((k < (mp3_bytes - 4)).toInt().toInt()))).toBool())) {
                        if (hdr_compare(mp3, (mp3 + k)).toBool()) {
                            val __oldPos2 = STACK_PTR
                            try {
                                var fb: Int = (k - hdr_padding(mp3))
                                var nextfb: Int = (fb + hdr_padding((mp3 + k)))
                                if (((((i + k) + nextfb) + 4) > mp3_bytes) || (!(hdr_compare(mp3, ((mp3 + k) + nextfb)).toBool()))) {
                                    k += 1
                                    continue@while1
                                }
                                frame_and_padding = k
                                frame_bytes = fb
                                free_format_bytes.value = fb
                            }
                            finally {
                                STACK_PTR = __oldPos2
                            }
                        }
                        k += 1
                    }
                    if ((((frame_bytes.toBool()) && ((i + ((frame_and_padding <= mp3_bytes).toInt().toInt())).toBool())) && (mp3d_match_frame(mp3, (mp3_bytes - i), frame_bytes).toBool())) || ((!(i.toBool())) && (frame_and_padding == mp3_bytes))) {
                        ptr_frame_bytes.value = frame_and_padding
                        return i
                    }
                    free_format_bytes.value = 0
                }
                finally {
                    STACK_PTR = __oldPos1
                }
            }
            run { i++; mp3.also { mp3 += 1 } }
        }
        ptr_frame_bytes.value = 0
        return i
    }
    fun mp3dec_init(dec: CPointer<mp3dec_t>): Unit = stackFrame {
        dec.value.header[0] = 0.toUByte()
    }
    fun mp3dec_decode_frame(dec: CPointer<mp3dec_t>, mp3: CPointer<UByte>, mp3_bytes: Int, pcm: CPointer<Short>, info: CPointer<mp3dec_frame_info_t>): Int = stackFrame {
        // Require alloc in stack to get pointer: frame_size
        // Require alloc in stack to get pointer: scratch
        var pcm = pcm // Mutating parameter
        var i: Int = 0
        var igr: Int = 0
        var frame_size: CPointer<Int> = alloca(4).toCPointer<Int>().also { it.value = 0 }
        var success: Int = 1
        var hdr: CPointer<UByte> = CPointer(0)
        var bs_frame: Array1bs_t = Array1bs_tAlloc((bs_t(0)))
        var scratch: mp3dec_scratch_t = mp3dec_scratch_tAlloc().copyFrom(mp3dec_scratch_tAlloc())
        if (((mp3_bytes > 4) && ((dec.value.header[0].toInt()) == 255)) && (hdr_compare((CPointer<UByte>((dec.value.header).ptr)), mp3).toBool())) {
            frame_size.value = hdr_frame_bytes(mp3, dec.value.free_format_bytes) + hdr_padding(mp3)
            if ((frame_size.value != mp3_bytes) && (((frame_size.value + 4) > mp3_bytes) || (!(hdr_compare(mp3, (mp3 + frame_size.value)).toBool())))) {
                frame_size.value = 0
            }
        }
        if (!(frame_size.value.toBool())) {
            memset((CPointer<Unit>((dec).ptr)), 0, mp3dec_t.SIZE_BYTES)
            i = mp3d_find_frame(mp3, mp3_bytes, (CPointer((dec).ptr + mp3dec_t.OFFSET_free_format_bytes)), (CPointer<Int>((frame_size).ptr)))
            if ((!(frame_size.value.toBool())) || ((i + ((frame_size.value > mp3_bytes).toInt().toInt())).toBool())) {
                info.value.frame_bytes = i
                return 0
            }
        }
        hdr = mp3 + i
        memcpy((CPointer<Unit>((dec.value.header).ptr)), (CPointer<Unit>((hdr).ptr)), 4)
        info.value.frame_bytes = i + frame_size.value
        info.value.channels = (if ((((hdr[3].toUInt()) and (192.toUInt())).toInt()) == 192) 1 else 2)
        info.value.hz = hdr_sample_rate_hz(hdr).toInt()
        info.value.layer = 4 - ((((hdr[1].toUInt()) shr (1).toInt()) and (3.toUInt())).toInt())
        info.value.bitrate_kbps = hdr_bitrate_kbps(hdr).toInt()
        if (!(pcm.toBool())) {
            return hdr_frame_samples(hdr).toInt()
        }
        bs_init((CPointer<bs_t>((bs_frame).ptr)), (hdr + 4), (frame_size.value - 4))
        if (!(((hdr[1].toUInt()) and (1.toUInt())).toBool())) {
            get_bits((CPointer<bs_t>((bs_frame).ptr)), 16)
        }
        if (info.value.layer == 3) {
            stackFrame {
                var main_data_begin: Int = L3_read_side_info((CPointer<bs_t>((bs_frame).ptr)), (CPointer<L3_gr_info_t>((scratch.gr_info).ptr)), hdr)
                if ((main_data_begin < 0) || (bs_frame.value.pos > bs_frame.value.limit)) {
                    mp3dec_init(dec)
                    return 0
                }
                success = L3_restore_reservoir(dec, (CPointer<bs_t>((bs_frame).ptr)), (CPointer<mp3dec_scratch_t>((scratch).ptr)), main_data_begin)
                if (success.toBool()) {
                    igr = 0
                    while (igr < (if (((hdr[1].toUInt()) and (8.toUInt())).toBool()) 2 else 1)) {
                        memset((CPointer<Unit>((scratch.grbuf[0]).ptr)), 0, ((576 * 2) * Float.SIZE_BYTES))
                        L3_decode(dec, (CPointer<mp3dec_scratch_t>((scratch).ptr)), (scratch.gr_info + (igr * info.value.channels)), info.value.channels)
                        mp3d_synth_granule((CPointer<Float>((dec.value.qmf_state).ptr)), (CPointer<Float>((scratch.grbuf[0]).ptr)), 18, info.value.channels, pcm, (CPointer<Float>((scratch.syn[0]).ptr)))
                        run { igr++; run { pcm + (576 * info.value.channels) }.also { `$` -> pcm = `$` } }
                    }
                }
                L3_save_reservoir(dec, (CPointer<mp3dec_scratch_t>((scratch).ptr)))
            }
        } else {
            stackFrame {
                var sci: Array1L12_scale_info = Array1L12_scale_infoAlloc((L12_scale_info(0)))
                L12_read_scale_info(hdr, (CPointer<bs_t>((bs_frame).ptr)), (CPointer<L12_scale_info>((sci).ptr)))
                memset((CPointer<Unit>((scratch.grbuf[0]).ptr)), 0, ((576 * 2) * Float.SIZE_BYTES))
                run { run { 0 }.also { `$` -> i = `$` }; run { 0 }.also { `$` -> igr = `$` } }
                while (igr < 3) {
                    if (12 == (run { i + L12_dequantize_granule((scratch.grbuf[0] + i), (CPointer<bs_t>((bs_frame).ptr)), (CPointer<L12_scale_info>((sci).ptr)), (info.value.layer or 1)) }.also { `$` -> i = `$` })) {
                        i = 0
                        L12_apply_scf_384((CPointer<L12_scale_info>((sci).ptr)), (sci.value.scf + igr), (CPointer<Float>((scratch.grbuf[0]).ptr)))
                        mp3d_synth_granule((CPointer<Float>((dec.value.qmf_state).ptr)), (CPointer<Float>((scratch.grbuf[0]).ptr)), 12, info.value.channels, pcm, (CPointer<Float>((scratch.syn[0]).ptr)))
                        memset((CPointer<Unit>((scratch.grbuf[0]).ptr)), 0, ((576 * 2) * Float.SIZE_BYTES))
                        pcm = pcm + (384 * info.value.channels)
                    }
                    if (bs_frame.value.pos > bs_frame.value.limit) {
                        mp3dec_init(dec)
                        return 0
                    }
                    igr += 1
                }
            }
        }
        return success * (hdr_frame_samples((CPointer<UByte>((dec.value.header).ptr))).toInt())
    }

    //////////////////
    // C STRUCTURES //
    //////////////////

    /*!inline*/ class mp3dec_frame_info_t(val ptr: Int) : IStruct {
        companion object : IStructCompanion<mp3dec_frame_info_t>  {
            const val SIZE_BYTES = 20
            override val SIZE = SIZE_BYTES
            const val OFFSET_frame_bytes = 0
            const val OFFSET_channels = 4
            const val OFFSET_hz = 8
            const val OFFSET_layer = 12
            const val OFFSET_bitrate_kbps = 16
        }
    }
    fun mp3dec_frame_info_tAlloc(): mp3dec_frame_info_t = mp3dec_frame_info_t(alloca(mp3dec_frame_info_t.SIZE_BYTES).ptr)
    fun mp3dec_frame_info_tAlloc(frame_bytes: Int, channels: Int, hz: Int, layer: Int, bitrate_kbps: Int): mp3dec_frame_info_t = mp3dec_frame_info_tAlloc().apply { this.frame_bytes = frame_bytes; this.channels = channels; this.hz = hz; this.layer = layer; this.bitrate_kbps = bitrate_kbps }
    fun mp3dec_frame_info_t.copyFrom(src: mp3dec_frame_info_t): mp3dec_frame_info_t = this.apply { memcpy(CPointer<Unit>(this.ptr), CPointer<Unit>(src.ptr), mp3dec_frame_info_t.SIZE_BYTES) }
    fun fixedArrayOfmp3dec_frame_info_t(size: Int, vararg items: mp3dec_frame_info_t): CPointer<mp3dec_frame_info_t> = alloca_zero(size * mp3dec_frame_info_t.SIZE_BYTES).toCPointer<mp3dec_frame_info_t>().also { for (n in 0 until items.size) mp3dec_frame_info_t(it.ptr + n * mp3dec_frame_info_t.SIZE_BYTES).copyFrom(items[n]) }
    operator fun CPointer<mp3dec_frame_info_t>.get(index: Int): mp3dec_frame_info_t = mp3dec_frame_info_t(this.ptr + index * mp3dec_frame_info_t.SIZE_BYTES)
    operator fun CPointer<mp3dec_frame_info_t>.set(index: Int, value: mp3dec_frame_info_t) = mp3dec_frame_info_t(this.ptr + index * mp3dec_frame_info_t.SIZE_BYTES).copyFrom(value)
    @JvmName("plusmp3dec_frame_info_t") operator fun CPointer<mp3dec_frame_info_t>.plus(offset: Int): CPointer<mp3dec_frame_info_t> = CPointer(this.ptr + offset * mp3dec_frame_info_t.SIZE_BYTES)
    @JvmName("minusmp3dec_frame_info_t") operator fun CPointer<mp3dec_frame_info_t>.minus(offset: Int): CPointer<mp3dec_frame_info_t> = CPointer(this.ptr - offset * mp3dec_frame_info_t.SIZE_BYTES)
    @JvmName("minusPtrmp3dec_frame_info_t") operator fun CPointer<mp3dec_frame_info_t>.minus(other: CPointer<mp3dec_frame_info_t>) = (this.ptr - other.ptr) / mp3dec_frame_info_t.SIZE_BYTES
    var CPointer<mp3dec_frame_info_t>.value: mp3dec_frame_info_t get() = this[0]; set(value) = run { this[0] = value }
    var mp3dec_frame_info_t.frame_bytes: Int get() = lw(ptr + mp3dec_frame_info_t.OFFSET_frame_bytes); set(value) = sw(ptr + mp3dec_frame_info_t.OFFSET_frame_bytes, value)
    var mp3dec_frame_info_t.channels: Int get() = lw(ptr + mp3dec_frame_info_t.OFFSET_channels); set(value) = sw(ptr + mp3dec_frame_info_t.OFFSET_channels, value)
    var mp3dec_frame_info_t.hz: Int get() = lw(ptr + mp3dec_frame_info_t.OFFSET_hz); set(value) = sw(ptr + mp3dec_frame_info_t.OFFSET_hz, value)
    var mp3dec_frame_info_t.layer: Int get() = lw(ptr + mp3dec_frame_info_t.OFFSET_layer); set(value) = sw(ptr + mp3dec_frame_info_t.OFFSET_layer, value)
    var mp3dec_frame_info_t.bitrate_kbps: Int get() = lw(ptr + mp3dec_frame_info_t.OFFSET_bitrate_kbps); set(value) = sw(ptr + mp3dec_frame_info_t.OFFSET_bitrate_kbps, value)
    /*!inline*/ class mp3dec_t(val ptr: Int) : IStruct {
        companion object : IStructCompanion<mp3dec_t>  {
            const val SIZE_BYTES = 6667
            override val SIZE = SIZE_BYTES
            const val OFFSET_mdct_overlap = 0
            const val OFFSET_qmf_state = 2304
            const val OFFSET_reserv = 6144
            const val OFFSET_free_format_bytes = 6148
            const val OFFSET_header = 6152
            const val OFFSET_reserv_buf = 6156
        }
    }
    fun mp3dec_tAlloc(): mp3dec_t = mp3dec_t(alloca(mp3dec_t.SIZE_BYTES).ptr)
    fun mp3dec_tAlloc(mdct_overlap: Array2Array288Float, qmf_state: Array960Float, reserv: Int, free_format_bytes: Int, header: Array4UByte, reserv_buf: Array511UByte): mp3dec_t = mp3dec_tAlloc().apply { this.mdct_overlap = mdct_overlap; this.qmf_state = qmf_state; this.reserv = reserv; this.free_format_bytes = free_format_bytes; this.header = header; this.reserv_buf = reserv_buf }
    fun mp3dec_t.copyFrom(src: mp3dec_t): mp3dec_t = this.apply { memcpy(CPointer<Unit>(this.ptr), CPointer<Unit>(src.ptr), mp3dec_t.SIZE_BYTES) }
    fun fixedArrayOfmp3dec_t(size: Int, vararg items: mp3dec_t): CPointer<mp3dec_t> = alloca_zero(size * mp3dec_t.SIZE_BYTES).toCPointer<mp3dec_t>().also { for (n in 0 until items.size) mp3dec_t(it.ptr + n * mp3dec_t.SIZE_BYTES).copyFrom(items[n]) }
    operator fun CPointer<mp3dec_t>.get(index: Int): mp3dec_t = mp3dec_t(this.ptr + index * mp3dec_t.SIZE_BYTES)
    operator fun CPointer<mp3dec_t>.set(index: Int, value: mp3dec_t) = mp3dec_t(this.ptr + index * mp3dec_t.SIZE_BYTES).copyFrom(value)
    @JvmName("plusmp3dec_t") operator fun CPointer<mp3dec_t>.plus(offset: Int): CPointer<mp3dec_t> = CPointer(this.ptr + offset * mp3dec_t.SIZE_BYTES)
    @JvmName("minusmp3dec_t") operator fun CPointer<mp3dec_t>.minus(offset: Int): CPointer<mp3dec_t> = CPointer(this.ptr - offset * mp3dec_t.SIZE_BYTES)
    @JvmName("minusPtrmp3dec_t") operator fun CPointer<mp3dec_t>.minus(other: CPointer<mp3dec_t>) = (this.ptr - other.ptr) / mp3dec_t.SIZE_BYTES
    var CPointer<mp3dec_t>.value: mp3dec_t get() = this[0]; set(value) = run { this[0] = value }
    var mp3dec_t.mdct_overlap: Array2Array288Float get() = Array2Array288Float(ptr + mp3dec_t.OFFSET_mdct_overlap); set(value) = run { TODO("Unsupported setting ftype=Float[288][2]") }
    var mp3dec_t.qmf_state: Array960Float get() = Array960Float(ptr + mp3dec_t.OFFSET_qmf_state); set(value) = run { TODO("Unsupported setting ftype=Float[960]") }
    var mp3dec_t.reserv: Int get() = lw(ptr + mp3dec_t.OFFSET_reserv); set(value) = sw(ptr + mp3dec_t.OFFSET_reserv, value)
    var mp3dec_t.free_format_bytes: Int get() = lw(ptr + mp3dec_t.OFFSET_free_format_bytes); set(value) = sw(ptr + mp3dec_t.OFFSET_free_format_bytes, value)
    var mp3dec_t.header: Array4UByte get() = Array4UByte(ptr + mp3dec_t.OFFSET_header); set(value) = run { TODO("Unsupported setting ftype=UByte[4]") }
    var mp3dec_t.reserv_buf: Array511UByte get() = Array511UByte(ptr + mp3dec_t.OFFSET_reserv_buf); set(value) = run { TODO("Unsupported setting ftype=UByte[511]") }
    /*!inline*/ class bs_t(val ptr: Int) : IStruct {
        companion object : IStructCompanion<bs_t>  {
            const val SIZE_BYTES = 12
            override val SIZE = SIZE_BYTES
            const val OFFSET_buf = 0
            const val OFFSET_pos = 4
            const val OFFSET_limit = 8
        }
    }
    fun bs_tAlloc(): bs_t = bs_t(alloca(bs_t.SIZE_BYTES).ptr)
    fun bs_tAlloc(buf: CPointer<UByte>, pos: Int, limit: Int): bs_t = bs_tAlloc().apply { this.buf = buf; this.pos = pos; this.limit = limit }
    fun bs_t.copyFrom(src: bs_t): bs_t = this.apply { memcpy(CPointer<Unit>(this.ptr), CPointer<Unit>(src.ptr), bs_t.SIZE_BYTES) }
    fun fixedArrayOfbs_t(size: Int, vararg items: bs_t): CPointer<bs_t> = alloca_zero(size * bs_t.SIZE_BYTES).toCPointer<bs_t>().also { for (n in 0 until items.size) bs_t(it.ptr + n * bs_t.SIZE_BYTES).copyFrom(items[n]) }
    operator fun CPointer<bs_t>.get(index: Int): bs_t = bs_t(this.ptr + index * bs_t.SIZE_BYTES)
    operator fun CPointer<bs_t>.set(index: Int, value: bs_t) = bs_t(this.ptr + index * bs_t.SIZE_BYTES).copyFrom(value)
    @JvmName("plusbs_t") operator fun CPointer<bs_t>.plus(offset: Int): CPointer<bs_t> = CPointer(this.ptr + offset * bs_t.SIZE_BYTES)
    @JvmName("minusbs_t") operator fun CPointer<bs_t>.minus(offset: Int): CPointer<bs_t> = CPointer(this.ptr - offset * bs_t.SIZE_BYTES)
    @JvmName("minusPtrbs_t") operator fun CPointer<bs_t>.minus(other: CPointer<bs_t>) = (this.ptr - other.ptr) / bs_t.SIZE_BYTES
    var CPointer<bs_t>.value: bs_t get() = this[0]; set(value) = run { this[0] = value }
    var bs_t.buf: CPointer<UByte> get() = CPointer(lw(ptr + bs_t.OFFSET_buf)); set(value) = run { sw(ptr + bs_t.OFFSET_buf, value.ptr) }
    var bs_t.pos: Int get() = lw(ptr + bs_t.OFFSET_pos); set(value) = sw(ptr + bs_t.OFFSET_pos, value)
    var bs_t.limit: Int get() = lw(ptr + bs_t.OFFSET_limit); set(value) = sw(ptr + bs_t.OFFSET_limit, value)
    /*!inline*/ class L12_scale_info(val ptr: Int) : IStruct {
        companion object : IStructCompanion<L12_scale_info>  {
            const val SIZE_BYTES = 898
            override val SIZE = SIZE_BYTES
            const val OFFSET_scf = 0
            const val OFFSET_total_bands = 768
            const val OFFSET_stereo_bands = 769
            const val OFFSET_bitalloc = 770
            const val OFFSET_scfcod = 834
        }
    }
    fun L12_scale_infoAlloc(): L12_scale_info = L12_scale_info(alloca(L12_scale_info.SIZE_BYTES).ptr)
    fun L12_scale_infoAlloc(scf: Array192Float, total_bands: UByte, stereo_bands: UByte, bitalloc: Array64UByte, scfcod: Array64UByte): L12_scale_info = L12_scale_infoAlloc().apply { this.scf = scf; this.total_bands = total_bands; this.stereo_bands = stereo_bands; this.bitalloc = bitalloc; this.scfcod = scfcod }
    fun L12_scale_info.copyFrom(src: L12_scale_info): L12_scale_info = this.apply { memcpy(CPointer<Unit>(this.ptr), CPointer<Unit>(src.ptr), L12_scale_info.SIZE_BYTES) }
    fun fixedArrayOfL12_scale_info(size: Int, vararg items: L12_scale_info): CPointer<L12_scale_info> = alloca_zero(size * L12_scale_info.SIZE_BYTES).toCPointer<L12_scale_info>().also { for (n in 0 until items.size) L12_scale_info(it.ptr + n * L12_scale_info.SIZE_BYTES).copyFrom(items[n]) }
    operator fun CPointer<L12_scale_info>.get(index: Int): L12_scale_info = L12_scale_info(this.ptr + index * L12_scale_info.SIZE_BYTES)
    operator fun CPointer<L12_scale_info>.set(index: Int, value: L12_scale_info) = L12_scale_info(this.ptr + index * L12_scale_info.SIZE_BYTES).copyFrom(value)
    @JvmName("plusL12_scale_info") operator fun CPointer<L12_scale_info>.plus(offset: Int): CPointer<L12_scale_info> = CPointer(this.ptr + offset * L12_scale_info.SIZE_BYTES)
    @JvmName("minusL12_scale_info") operator fun CPointer<L12_scale_info>.minus(offset: Int): CPointer<L12_scale_info> = CPointer(this.ptr - offset * L12_scale_info.SIZE_BYTES)
    @JvmName("minusPtrL12_scale_info") operator fun CPointer<L12_scale_info>.minus(other: CPointer<L12_scale_info>) = (this.ptr - other.ptr) / L12_scale_info.SIZE_BYTES
    var CPointer<L12_scale_info>.value: L12_scale_info get() = this[0]; set(value) = run { this[0] = value }
    var L12_scale_info.scf: Array192Float get() = Array192Float(ptr + L12_scale_info.OFFSET_scf); set(value) = run { TODO("Unsupported setting ftype=Float[192]") }
    var L12_scale_info.total_bands: UByte get() = lb(ptr + L12_scale_info.OFFSET_total_bands).toUByte(); set(value) = sb(ptr + L12_scale_info.OFFSET_total_bands, (value).toByte())
    var L12_scale_info.stereo_bands: UByte get() = lb(ptr + L12_scale_info.OFFSET_stereo_bands).toUByte(); set(value) = sb(ptr + L12_scale_info.OFFSET_stereo_bands, (value).toByte())
    var L12_scale_info.bitalloc: Array64UByte get() = Array64UByte(ptr + L12_scale_info.OFFSET_bitalloc); set(value) = run { TODO("Unsupported setting ftype=UByte[64]") }
    var L12_scale_info.scfcod: Array64UByte get() = Array64UByte(ptr + L12_scale_info.OFFSET_scfcod); set(value) = run { TODO("Unsupported setting ftype=UByte[64]") }
    /*!inline*/ class L12_subband_alloc_t(val ptr: Int) : IStruct {
        companion object : IStructCompanion<L12_subband_alloc_t>  {
            const val SIZE_BYTES = 3
            override val SIZE = SIZE_BYTES
            const val OFFSET_tab_offset = 0
            const val OFFSET_code_tab_width = 1
            const val OFFSET_band_count = 2
        }
    }
    fun L12_subband_alloc_tAlloc(): L12_subband_alloc_t = L12_subband_alloc_t(alloca(L12_subband_alloc_t.SIZE_BYTES).ptr)
    fun L12_subband_alloc_tAlloc(tab_offset: UByte, code_tab_width: UByte, band_count: UByte): L12_subband_alloc_t = L12_subband_alloc_tAlloc().apply { this.tab_offset = tab_offset; this.code_tab_width = code_tab_width; this.band_count = band_count }
    fun L12_subband_alloc_t.copyFrom(src: L12_subband_alloc_t): L12_subband_alloc_t = this.apply { memcpy(CPointer<Unit>(this.ptr), CPointer<Unit>(src.ptr), L12_subband_alloc_t.SIZE_BYTES) }
    fun fixedArrayOfL12_subband_alloc_t(size: Int, vararg items: L12_subband_alloc_t): CPointer<L12_subband_alloc_t> = alloca_zero(size * L12_subband_alloc_t.SIZE_BYTES).toCPointer<L12_subband_alloc_t>().also { for (n in 0 until items.size) L12_subband_alloc_t(it.ptr + n * L12_subband_alloc_t.SIZE_BYTES).copyFrom(items[n]) }
    operator fun CPointer<L12_subband_alloc_t>.get(index: Int): L12_subband_alloc_t = L12_subband_alloc_t(this.ptr + index * L12_subband_alloc_t.SIZE_BYTES)
    operator fun CPointer<L12_subband_alloc_t>.set(index: Int, value: L12_subband_alloc_t) = L12_subband_alloc_t(this.ptr + index * L12_subband_alloc_t.SIZE_BYTES).copyFrom(value)
    @JvmName("plusL12_subband_alloc_t") operator fun CPointer<L12_subband_alloc_t>.plus(offset: Int): CPointer<L12_subband_alloc_t> = CPointer(this.ptr + offset * L12_subband_alloc_t.SIZE_BYTES)
    @JvmName("minusL12_subband_alloc_t") operator fun CPointer<L12_subband_alloc_t>.minus(offset: Int): CPointer<L12_subband_alloc_t> = CPointer(this.ptr - offset * L12_subband_alloc_t.SIZE_BYTES)
    @JvmName("minusPtrL12_subband_alloc_t") operator fun CPointer<L12_subband_alloc_t>.minus(other: CPointer<L12_subband_alloc_t>) = (this.ptr - other.ptr) / L12_subband_alloc_t.SIZE_BYTES
    var CPointer<L12_subband_alloc_t>.value: L12_subband_alloc_t get() = this[0]; set(value) = run { this[0] = value }
    var L12_subband_alloc_t.tab_offset: UByte get() = lb(ptr + L12_subband_alloc_t.OFFSET_tab_offset).toUByte(); set(value) = sb(ptr + L12_subband_alloc_t.OFFSET_tab_offset, (value).toByte())
    var L12_subband_alloc_t.code_tab_width: UByte get() = lb(ptr + L12_subband_alloc_t.OFFSET_code_tab_width).toUByte(); set(value) = sb(ptr + L12_subband_alloc_t.OFFSET_code_tab_width, (value).toByte())
    var L12_subband_alloc_t.band_count: UByte get() = lb(ptr + L12_subband_alloc_t.OFFSET_band_count).toUByte(); set(value) = sb(ptr + L12_subband_alloc_t.OFFSET_band_count, (value).toByte())
    /*!inline*/ class L3_gr_info_t(val ptr: Int) : IStruct {
        companion object : IStructCompanion<L3_gr_info_t>  {
            const val SIZE_BYTES = 28
            override val SIZE = SIZE_BYTES
            const val OFFSET_sfbtab = 0
            const val OFFSET_part_23_length = 4
            const val OFFSET_big_values = 6
            const val OFFSET_scalefac_compress = 8
            const val OFFSET_global_gain = 10
            const val OFFSET_block_type = 11
            const val OFFSET_mixed_block_flag = 12
            const val OFFSET_n_long_sfb = 13
            const val OFFSET_n_short_sfb = 14
            const val OFFSET_table_select = 15
            const val OFFSET_region_count = 18
            const val OFFSET_subblock_gain = 21
            const val OFFSET_preflag = 24
            const val OFFSET_scalefac_scale = 25
            const val OFFSET_count1_table = 26
            const val OFFSET_scfsi = 27
        }
    }
    fun L3_gr_info_tAlloc(): L3_gr_info_t = L3_gr_info_t(alloca(L3_gr_info_t.SIZE_BYTES).ptr)
    fun L3_gr_info_tAlloc(sfbtab: CPointer<UByte>, part_23_length: UShort, big_values: UShort, scalefac_compress: UShort, global_gain: UByte, block_type: UByte, mixed_block_flag: UByte, n_long_sfb: UByte, n_short_sfb: UByte, table_select: Array3UByte, region_count: Array3UByte, subblock_gain: Array3UByte, preflag: UByte, scalefac_scale: UByte, count1_table: UByte, scfsi: UByte): L3_gr_info_t = L3_gr_info_tAlloc().apply { this.sfbtab = sfbtab; this.part_23_length = part_23_length; this.big_values = big_values; this.scalefac_compress = scalefac_compress; this.global_gain = global_gain; this.block_type = block_type; this.mixed_block_flag = mixed_block_flag; this.n_long_sfb = n_long_sfb; this.n_short_sfb = n_short_sfb; this.table_select = table_select; this.region_count = region_count; this.subblock_gain = subblock_gain; this.preflag = preflag; this.scalefac_scale = scalefac_scale; this.count1_table = count1_table; this.scfsi = scfsi }
    fun L3_gr_info_t.copyFrom(src: L3_gr_info_t): L3_gr_info_t = this.apply { memcpy(CPointer<Unit>(this.ptr), CPointer<Unit>(src.ptr), L3_gr_info_t.SIZE_BYTES) }
    fun fixedArrayOfL3_gr_info_t(size: Int, vararg items: L3_gr_info_t): CPointer<L3_gr_info_t> = alloca_zero(size * L3_gr_info_t.SIZE_BYTES).toCPointer<L3_gr_info_t>().also { for (n in 0 until items.size) L3_gr_info_t(it.ptr + n * L3_gr_info_t.SIZE_BYTES).copyFrom(items[n]) }
    operator fun CPointer<L3_gr_info_t>.get(index: Int): L3_gr_info_t = L3_gr_info_t(this.ptr + index * L3_gr_info_t.SIZE_BYTES)
    operator fun CPointer<L3_gr_info_t>.set(index: Int, value: L3_gr_info_t) = L3_gr_info_t(this.ptr + index * L3_gr_info_t.SIZE_BYTES).copyFrom(value)
    @JvmName("plusL3_gr_info_t") operator fun CPointer<L3_gr_info_t>.plus(offset: Int): CPointer<L3_gr_info_t> = CPointer(this.ptr + offset * L3_gr_info_t.SIZE_BYTES)
    @JvmName("minusL3_gr_info_t") operator fun CPointer<L3_gr_info_t>.minus(offset: Int): CPointer<L3_gr_info_t> = CPointer(this.ptr - offset * L3_gr_info_t.SIZE_BYTES)
    @JvmName("minusPtrL3_gr_info_t") operator fun CPointer<L3_gr_info_t>.minus(other: CPointer<L3_gr_info_t>) = (this.ptr - other.ptr) / L3_gr_info_t.SIZE_BYTES
    var CPointer<L3_gr_info_t>.value: L3_gr_info_t get() = this[0]; set(value) = run { this[0] = value }
    var L3_gr_info_t.sfbtab: CPointer<UByte> get() = CPointer(lw(ptr + L3_gr_info_t.OFFSET_sfbtab)); set(value) = run { sw(ptr + L3_gr_info_t.OFFSET_sfbtab, value.ptr) }
    var L3_gr_info_t.part_23_length: UShort get() = lh(ptr + L3_gr_info_t.OFFSET_part_23_length).toUShort(); set(value) = sh(ptr + L3_gr_info_t.OFFSET_part_23_length, (value).toShort())
    var L3_gr_info_t.big_values: UShort get() = lh(ptr + L3_gr_info_t.OFFSET_big_values).toUShort(); set(value) = sh(ptr + L3_gr_info_t.OFFSET_big_values, (value).toShort())
    var L3_gr_info_t.scalefac_compress: UShort get() = lh(ptr + L3_gr_info_t.OFFSET_scalefac_compress).toUShort(); set(value) = sh(ptr + L3_gr_info_t.OFFSET_scalefac_compress, (value).toShort())
    var L3_gr_info_t.global_gain: UByte get() = lb(ptr + L3_gr_info_t.OFFSET_global_gain).toUByte(); set(value) = sb(ptr + L3_gr_info_t.OFFSET_global_gain, (value).toByte())
    var L3_gr_info_t.block_type: UByte get() = lb(ptr + L3_gr_info_t.OFFSET_block_type).toUByte(); set(value) = sb(ptr + L3_gr_info_t.OFFSET_block_type, (value).toByte())
    var L3_gr_info_t.mixed_block_flag: UByte get() = lb(ptr + L3_gr_info_t.OFFSET_mixed_block_flag).toUByte(); set(value) = sb(ptr + L3_gr_info_t.OFFSET_mixed_block_flag, (value).toByte())
    var L3_gr_info_t.n_long_sfb: UByte get() = lb(ptr + L3_gr_info_t.OFFSET_n_long_sfb).toUByte(); set(value) = sb(ptr + L3_gr_info_t.OFFSET_n_long_sfb, (value).toByte())
    var L3_gr_info_t.n_short_sfb: UByte get() = lb(ptr + L3_gr_info_t.OFFSET_n_short_sfb).toUByte(); set(value) = sb(ptr + L3_gr_info_t.OFFSET_n_short_sfb, (value).toByte())
    var L3_gr_info_t.table_select: Array3UByte get() = Array3UByte(ptr + L3_gr_info_t.OFFSET_table_select); set(value) = run { TODO("Unsupported setting ftype=UByte[3]") }
    var L3_gr_info_t.region_count: Array3UByte get() = Array3UByte(ptr + L3_gr_info_t.OFFSET_region_count); set(value) = run { TODO("Unsupported setting ftype=UByte[3]") }
    var L3_gr_info_t.subblock_gain: Array3UByte get() = Array3UByte(ptr + L3_gr_info_t.OFFSET_subblock_gain); set(value) = run { TODO("Unsupported setting ftype=UByte[3]") }
    var L3_gr_info_t.preflag: UByte get() = lb(ptr + L3_gr_info_t.OFFSET_preflag).toUByte(); set(value) = sb(ptr + L3_gr_info_t.OFFSET_preflag, (value).toByte())
    var L3_gr_info_t.scalefac_scale: UByte get() = lb(ptr + L3_gr_info_t.OFFSET_scalefac_scale).toUByte(); set(value) = sb(ptr + L3_gr_info_t.OFFSET_scalefac_scale, (value).toByte())
    var L3_gr_info_t.count1_table: UByte get() = lb(ptr + L3_gr_info_t.OFFSET_count1_table).toUByte(); set(value) = sb(ptr + L3_gr_info_t.OFFSET_count1_table, (value).toByte())
    var L3_gr_info_t.scfsi: UByte get() = lb(ptr + L3_gr_info_t.OFFSET_scfsi).toUByte(); set(value) = sb(ptr + L3_gr_info_t.OFFSET_scfsi, (value).toByte())
    /*!inline*/ class mp3dec_scratch_t(val ptr: Int) : IStruct {
        companion object : IStructCompanion<mp3dec_scratch_t>  {
            const val SIZE_BYTES = 16233
            override val SIZE = SIZE_BYTES
            const val OFFSET_bs = 0
            const val OFFSET_maindata = 12
            const val OFFSET_gr_info = 2827
            const val OFFSET_grbuf = 2939
            const val OFFSET_scf = 7547
            const val OFFSET_syn = 7707
            const val OFFSET_ist_pos = 16155
        }
    }
    fun mp3dec_scratch_tAlloc(): mp3dec_scratch_t = mp3dec_scratch_t(alloca(mp3dec_scratch_t.SIZE_BYTES).ptr)
    fun mp3dec_scratch_tAlloc(bs: bs_t, maindata: Array2815UByte, gr_info: Array4L3_gr_info_t, grbuf: Array2Array576Float, scf: Array40Float, syn: Array33Array64Float, ist_pos: Array2Array39UByte): mp3dec_scratch_t = mp3dec_scratch_tAlloc().apply { this.bs = bs; this.maindata = maindata; this.gr_info = gr_info; this.grbuf = grbuf; this.scf = scf; this.syn = syn; this.ist_pos = ist_pos }
    fun mp3dec_scratch_t.copyFrom(src: mp3dec_scratch_t): mp3dec_scratch_t = this.apply { memcpy(CPointer<Unit>(this.ptr), CPointer<Unit>(src.ptr), mp3dec_scratch_t.SIZE_BYTES) }
    fun fixedArrayOfmp3dec_scratch_t(size: Int, vararg items: mp3dec_scratch_t): CPointer<mp3dec_scratch_t> = alloca_zero(size * mp3dec_scratch_t.SIZE_BYTES).toCPointer<mp3dec_scratch_t>().also { for (n in 0 until items.size) mp3dec_scratch_t(it.ptr + n * mp3dec_scratch_t.SIZE_BYTES).copyFrom(items[n]) }
    operator fun CPointer<mp3dec_scratch_t>.get(index: Int): mp3dec_scratch_t = mp3dec_scratch_t(this.ptr + index * mp3dec_scratch_t.SIZE_BYTES)
    operator fun CPointer<mp3dec_scratch_t>.set(index: Int, value: mp3dec_scratch_t) = mp3dec_scratch_t(this.ptr + index * mp3dec_scratch_t.SIZE_BYTES).copyFrom(value)
    @JvmName("plusmp3dec_scratch_t") operator fun CPointer<mp3dec_scratch_t>.plus(offset: Int): CPointer<mp3dec_scratch_t> = CPointer(this.ptr + offset * mp3dec_scratch_t.SIZE_BYTES)
    @JvmName("minusmp3dec_scratch_t") operator fun CPointer<mp3dec_scratch_t>.minus(offset: Int): CPointer<mp3dec_scratch_t> = CPointer(this.ptr - offset * mp3dec_scratch_t.SIZE_BYTES)
    @JvmName("minusPtrmp3dec_scratch_t") operator fun CPointer<mp3dec_scratch_t>.minus(other: CPointer<mp3dec_scratch_t>) = (this.ptr - other.ptr) / mp3dec_scratch_t.SIZE_BYTES
    var CPointer<mp3dec_scratch_t>.value: mp3dec_scratch_t get() = this[0]; set(value) = run { this[0] = value }
    var mp3dec_scratch_t.bs: bs_t get() = bs_t(ptr + mp3dec_scratch_t.OFFSET_bs); set(value) = run { bs_t(ptr + mp3dec_scratch_t.OFFSET_bs).copyFrom(value) }
    var mp3dec_scratch_t.maindata: Array2815UByte get() = Array2815UByte(ptr + mp3dec_scratch_t.OFFSET_maindata); set(value) = run { TODO("Unsupported setting ftype=UByte[2815]") }
    var mp3dec_scratch_t.gr_info: Array4L3_gr_info_t get() = Array4L3_gr_info_t(ptr + mp3dec_scratch_t.OFFSET_gr_info); set(value) = run { TODO("Unsupported setting ftype=struct null[4]") }
    var mp3dec_scratch_t.grbuf: Array2Array576Float get() = Array2Array576Float(ptr + mp3dec_scratch_t.OFFSET_grbuf); set(value) = run { TODO("Unsupported setting ftype=Float[576][2]") }
    var mp3dec_scratch_t.scf: Array40Float get() = Array40Float(ptr + mp3dec_scratch_t.OFFSET_scf); set(value) = run { TODO("Unsupported setting ftype=Float[40]") }
    var mp3dec_scratch_t.syn: Array33Array64Float get() = Array33Array64Float(ptr + mp3dec_scratch_t.OFFSET_syn); set(value) = run { TODO("Unsupported setting ftype=Float[64][33]") }
    var mp3dec_scratch_t.ist_pos: Array2Array39UByte get() = Array2Array39UByte(ptr + mp3dec_scratch_t.OFFSET_ist_pos); set(value) = run { TODO("Unsupported setting ftype=UByte[39][2]") }
    /*!inline*/ class Array2Array288Float(val ptr: Int) {
        companion object {
            const val NUM_ELEMENTS = 2
            const val ELEMENT_SIZE_BYTES = 1152
            const val TOTAL_SIZE_BYTES = /*2304*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
        }
        fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
    }
    operator fun Array2Array288Float.get(index: Int): Array288Float = Array288Float(addr(index))
    operator fun Array2Array288Float.set(index: Int, value: Array288Float): Unit = run { memcpy(CPointer(addr(index)), CPointer(value.ptr), Array2Array288Float.ELEMENT_SIZE_BYTES) }
    var Array2Array288Float.value get() = this[0]; set(value) = run { this[0] = value }
    fun Array2Array288FloatAlloc(vararg items: Array288Float): Array2Array288Float = Array2Array288Float(alloca_zero(Array2Array288Float.TOTAL_SIZE_BYTES).ptr).also { for (n in 0 until items.size) it[n] = items[n] }
    operator fun Array2Array288Float.plus(offset: Int): CPointer<Array288Float> = CPointer<Array288Float>(addr(offset))
    operator fun Array2Array288Float.minus(offset: Int): CPointer<Array288Float> = CPointer<Array288Float>(addr(-offset))
    /*!inline*/ class Array288Float(val ptr: Int) {
        companion object {
            const val NUM_ELEMENTS = 288
            const val ELEMENT_SIZE_BYTES = 4
            const val TOTAL_SIZE_BYTES = /*1152*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
        }
        fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
    }
    operator fun Array288Float.get(index: Int): Float = Float.fromBits(lw(addr(index)))
    operator fun Array288Float.set(index: Int, value: Float): Unit = run { sw(addr(index), (value).toBits()) }
    var Array288Float.value get() = this[0]; set(value) = run { this[0] = value }
    fun Array288FloatAlloc(vararg items: Float): Array288Float = Array288Float(alloca_zero(Array288Float.TOTAL_SIZE_BYTES).ptr).also { for (n in 0 until items.size) it[n] = items[n] }
    operator fun Array288Float.plus(offset: Int): CPointer<Float> = CPointer<Float>(addr(offset))
    operator fun Array288Float.minus(offset: Int): CPointer<Float> = CPointer<Float>(addr(-offset))
    /*!inline*/ class Array960Float(val ptr: Int) {
        companion object {
            const val NUM_ELEMENTS = 960
            const val ELEMENT_SIZE_BYTES = 4
            const val TOTAL_SIZE_BYTES = /*3840*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
        }
        fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
    }
    operator fun Array960Float.get(index: Int): Float = Float.fromBits(lw(addr(index)))
    operator fun Array960Float.set(index: Int, value: Float): Unit = run { sw(addr(index), (value).toBits()) }
    var Array960Float.value get() = this[0]; set(value) = run { this[0] = value }
    fun Array960FloatAlloc(vararg items: Float): Array960Float = Array960Float(alloca_zero(Array960Float.TOTAL_SIZE_BYTES).ptr).also { for (n in 0 until items.size) it[n] = items[n] }
    operator fun Array960Float.plus(offset: Int): CPointer<Float> = CPointer<Float>(addr(offset))
    operator fun Array960Float.minus(offset: Int): CPointer<Float> = CPointer<Float>(addr(-offset))
    /*!inline*/ class Array4UByte(val ptr: Int) {
        companion object {
            const val NUM_ELEMENTS = 4
            const val ELEMENT_SIZE_BYTES = 1
            const val TOTAL_SIZE_BYTES = /*4*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
        }
        fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
    }
    operator fun Array4UByte.get(index: Int): UByte = lb(addr(index)).toUByte()
    operator fun Array4UByte.set(index: Int, value: UByte): Unit = run { sb(addr(index), (value).toByte()) }
    var Array4UByte.value get() = this[0]; set(value) = run { this[0] = value }
    fun Array4UByteAlloc(vararg items: UByte): Array4UByte = Array4UByte(alloca_zero(Array4UByte.TOTAL_SIZE_BYTES).ptr).also { for (n in 0 until items.size) it[n] = items[n] }
    operator fun Array4UByte.plus(offset: Int): CPointer<UByte> = CPointer<UByte>(addr(offset))
    operator fun Array4UByte.minus(offset: Int): CPointer<UByte> = CPointer<UByte>(addr(-offset))
    /*!inline*/ class Array511UByte(val ptr: Int) {
        companion object {
            const val NUM_ELEMENTS = 511
            const val ELEMENT_SIZE_BYTES = 1
            const val TOTAL_SIZE_BYTES = /*511*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
        }
        fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
    }
    operator fun Array511UByte.get(index: Int): UByte = lb(addr(index)).toUByte()
    operator fun Array511UByte.set(index: Int, value: UByte): Unit = run { sb(addr(index), (value).toByte()) }
    var Array511UByte.value get() = this[0]; set(value) = run { this[0] = value }
    fun Array511UByteAlloc(vararg items: UByte): Array511UByte = Array511UByte(alloca_zero(Array511UByte.TOTAL_SIZE_BYTES).ptr).also { for (n in 0 until items.size) it[n] = items[n] }
    operator fun Array511UByte.plus(offset: Int): CPointer<UByte> = CPointer<UByte>(addr(offset))
    operator fun Array511UByte.minus(offset: Int): CPointer<UByte> = CPointer<UByte>(addr(-offset))
    /*!inline*/ class Array192Float(val ptr: Int) {
        companion object {
            const val NUM_ELEMENTS = 192
            const val ELEMENT_SIZE_BYTES = 4
            const val TOTAL_SIZE_BYTES = /*768*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
        }
        fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
    }
    operator fun Array192Float.get(index: Int): Float = Float.fromBits(lw(addr(index)))
    operator fun Array192Float.set(index: Int, value: Float): Unit = run { sw(addr(index), (value).toBits()) }
    var Array192Float.value get() = this[0]; set(value) = run { this[0] = value }
    fun Array192FloatAlloc(vararg items: Float): Array192Float = Array192Float(alloca_zero(Array192Float.TOTAL_SIZE_BYTES).ptr).also { for (n in 0 until items.size) it[n] = items[n] }
    operator fun Array192Float.plus(offset: Int): CPointer<Float> = CPointer<Float>(addr(offset))
    operator fun Array192Float.minus(offset: Int): CPointer<Float> = CPointer<Float>(addr(-offset))
    /*!inline*/ class Array64UByte(val ptr: Int) {
        companion object {
            const val NUM_ELEMENTS = 64
            const val ELEMENT_SIZE_BYTES = 1
            const val TOTAL_SIZE_BYTES = /*64*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
        }
        fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
    }
    operator fun Array64UByte.get(index: Int): UByte = lb(addr(index)).toUByte()
    operator fun Array64UByte.set(index: Int, value: UByte): Unit = run { sb(addr(index), (value).toByte()) }
    var Array64UByte.value get() = this[0]; set(value) = run { this[0] = value }
    fun Array64UByteAlloc(vararg items: UByte): Array64UByte = Array64UByte(alloca_zero(Array64UByte.TOTAL_SIZE_BYTES).ptr).also { for (n in 0 until items.size) it[n] = items[n] }
    operator fun Array64UByte.plus(offset: Int): CPointer<UByte> = CPointer<UByte>(addr(offset))
    operator fun Array64UByte.minus(offset: Int): CPointer<UByte> = CPointer<UByte>(addr(-offset))
    /*!inline*/ class Array3UByte(val ptr: Int) {
        companion object {
            const val NUM_ELEMENTS = 3
            const val ELEMENT_SIZE_BYTES = 1
            const val TOTAL_SIZE_BYTES = /*3*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
        }
        fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
    }
    operator fun Array3UByte.get(index: Int): UByte = lb(addr(index)).toUByte()
    operator fun Array3UByte.set(index: Int, value: UByte): Unit = run { sb(addr(index), (value).toByte()) }
    var Array3UByte.value get() = this[0]; set(value) = run { this[0] = value }
    fun Array3UByteAlloc(vararg items: UByte): Array3UByte = Array3UByte(alloca_zero(Array3UByte.TOTAL_SIZE_BYTES).ptr).also { for (n in 0 until items.size) it[n] = items[n] }
    operator fun Array3UByte.plus(offset: Int): CPointer<UByte> = CPointer<UByte>(addr(offset))
    operator fun Array3UByte.minus(offset: Int): CPointer<UByte> = CPointer<UByte>(addr(-offset))
    /*!inline*/ class Array2815UByte(val ptr: Int) {
        companion object {
            const val NUM_ELEMENTS = 2815
            const val ELEMENT_SIZE_BYTES = 1
            const val TOTAL_SIZE_BYTES = /*2815*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
        }
        fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
    }
    operator fun Array2815UByte.get(index: Int): UByte = lb(addr(index)).toUByte()
    operator fun Array2815UByte.set(index: Int, value: UByte): Unit = run { sb(addr(index), (value).toByte()) }
    var Array2815UByte.value get() = this[0]; set(value) = run { this[0] = value }
    fun Array2815UByteAlloc(vararg items: UByte): Array2815UByte = Array2815UByte(alloca_zero(Array2815UByte.TOTAL_SIZE_BYTES).ptr).also { for (n in 0 until items.size) it[n] = items[n] }
    operator fun Array2815UByte.plus(offset: Int): CPointer<UByte> = CPointer<UByte>(addr(offset))
    operator fun Array2815UByte.minus(offset: Int): CPointer<UByte> = CPointer<UByte>(addr(-offset))
    /*!inline*/ class Array4L3_gr_info_t(val ptr: Int) {
        companion object {
            const val NUM_ELEMENTS = 4
            const val ELEMENT_SIZE_BYTES = 28
            const val TOTAL_SIZE_BYTES = /*112*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
        }
        fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
    }
    operator fun Array4L3_gr_info_t.get(index: Int): L3_gr_info_t = L3_gr_info_t(addr(index))
    operator fun Array4L3_gr_info_t.set(index: Int, value: L3_gr_info_t): Unit = run { L3_gr_info_t(addr(index)).copyFrom(value) }
    var Array4L3_gr_info_t.value get() = this[0]; set(value) = run { this[0] = value }
    fun Array4L3_gr_info_tAlloc(vararg items: L3_gr_info_t): Array4L3_gr_info_t = Array4L3_gr_info_t(alloca_zero(Array4L3_gr_info_t.TOTAL_SIZE_BYTES).ptr).also { for (n in 0 until items.size) it[n] = items[n] }
    operator fun Array4L3_gr_info_t.plus(offset: Int): CPointer<L3_gr_info_t> = CPointer<L3_gr_info_t>(addr(offset))
    operator fun Array4L3_gr_info_t.minus(offset: Int): CPointer<L3_gr_info_t> = CPointer<L3_gr_info_t>(addr(-offset))
    /*!inline*/ class Array2Array576Float(val ptr: Int) {
        companion object {
            const val NUM_ELEMENTS = 2
            const val ELEMENT_SIZE_BYTES = 2304
            const val TOTAL_SIZE_BYTES = /*4608*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
        }
        fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
    }
    operator fun Array2Array576Float.get(index: Int): Array576Float = Array576Float(addr(index))
    operator fun Array2Array576Float.set(index: Int, value: Array576Float): Unit = run { memcpy(CPointer(addr(index)), CPointer(value.ptr), Array2Array576Float.ELEMENT_SIZE_BYTES) }
    var Array2Array576Float.value get() = this[0]; set(value) = run { this[0] = value }
    fun Array2Array576FloatAlloc(vararg items: Array576Float): Array2Array576Float = Array2Array576Float(alloca_zero(Array2Array576Float.TOTAL_SIZE_BYTES).ptr).also { for (n in 0 until items.size) it[n] = items[n] }
    operator fun Array2Array576Float.plus(offset: Int): CPointer<Array576Float> = CPointer<Array576Float>(addr(offset))
    operator fun Array2Array576Float.minus(offset: Int): CPointer<Array576Float> = CPointer<Array576Float>(addr(-offset))
    /*!inline*/ class Array576Float(val ptr: Int) {
        companion object {
            const val NUM_ELEMENTS = 576
            const val ELEMENT_SIZE_BYTES = 4
            const val TOTAL_SIZE_BYTES = /*2304*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
        }
        fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
    }
    operator fun Array576Float.get(index: Int): Float = Float.fromBits(lw(addr(index)))
    operator fun Array576Float.set(index: Int, value: Float): Unit = run { sw(addr(index), (value).toBits()) }
    var Array576Float.value get() = this[0]; set(value) = run { this[0] = value }
    fun Array576FloatAlloc(vararg items: Float): Array576Float = Array576Float(alloca_zero(Array576Float.TOTAL_SIZE_BYTES).ptr).also { for (n in 0 until items.size) it[n] = items[n] }
    operator fun Array576Float.plus(offset: Int): CPointer<Float> = CPointer<Float>(addr(offset))
    operator fun Array576Float.minus(offset: Int): CPointer<Float> = CPointer<Float>(addr(-offset))
    /*!inline*/ class Array40Float(val ptr: Int) {
        companion object {
            const val NUM_ELEMENTS = 40
            const val ELEMENT_SIZE_BYTES = 4
            const val TOTAL_SIZE_BYTES = /*160*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
        }
        fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
    }
    operator fun Array40Float.get(index: Int): Float = Float.fromBits(lw(addr(index)))
    operator fun Array40Float.set(index: Int, value: Float): Unit = run { sw(addr(index), (value).toBits()) }
    var Array40Float.value get() = this[0]; set(value) = run { this[0] = value }
    fun Array40FloatAlloc(vararg items: Float): Array40Float = Array40Float(alloca_zero(Array40Float.TOTAL_SIZE_BYTES).ptr).also { for (n in 0 until items.size) it[n] = items[n] }
    operator fun Array40Float.plus(offset: Int): CPointer<Float> = CPointer<Float>(addr(offset))
    operator fun Array40Float.minus(offset: Int): CPointer<Float> = CPointer<Float>(addr(-offset))
    /*!inline*/ class Array33Array64Float(val ptr: Int) {
        companion object {
            const val NUM_ELEMENTS = 33
            const val ELEMENT_SIZE_BYTES = 256
            const val TOTAL_SIZE_BYTES = /*8448*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
        }
        fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
    }
    operator fun Array33Array64Float.get(index: Int): Array64Float = Array64Float(addr(index))
    operator fun Array33Array64Float.set(index: Int, value: Array64Float): Unit = run { memcpy(CPointer(addr(index)), CPointer(value.ptr), Array33Array64Float.ELEMENT_SIZE_BYTES) }
    var Array33Array64Float.value get() = this[0]; set(value) = run { this[0] = value }
    fun Array33Array64FloatAlloc(vararg items: Array64Float): Array33Array64Float = Array33Array64Float(alloca_zero(Array33Array64Float.TOTAL_SIZE_BYTES).ptr).also { for (n in 0 until items.size) it[n] = items[n] }
    operator fun Array33Array64Float.plus(offset: Int): CPointer<Array64Float> = CPointer<Array64Float>(addr(offset))
    operator fun Array33Array64Float.minus(offset: Int): CPointer<Array64Float> = CPointer<Array64Float>(addr(-offset))
    /*!inline*/ class Array64Float(val ptr: Int) {
        companion object {
            const val NUM_ELEMENTS = 64
            const val ELEMENT_SIZE_BYTES = 4
            const val TOTAL_SIZE_BYTES = /*256*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
        }
        fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
    }
    operator fun Array64Float.get(index: Int): Float = Float.fromBits(lw(addr(index)))
    operator fun Array64Float.set(index: Int, value: Float): Unit = run { sw(addr(index), (value).toBits()) }
    var Array64Float.value get() = this[0]; set(value) = run { this[0] = value }
    fun Array64FloatAlloc(vararg items: Float): Array64Float = Array64Float(alloca_zero(Array64Float.TOTAL_SIZE_BYTES).ptr).also { for (n in 0 until items.size) it[n] = items[n] }
    operator fun Array64Float.plus(offset: Int): CPointer<Float> = CPointer<Float>(addr(offset))
    operator fun Array64Float.minus(offset: Int): CPointer<Float> = CPointer<Float>(addr(-offset))
    /*!inline*/ class Array2Array39UByte(val ptr: Int) {
        companion object {
            const val NUM_ELEMENTS = 2
            const val ELEMENT_SIZE_BYTES = 39
            const val TOTAL_SIZE_BYTES = /*78*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
        }
        fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
    }
    operator fun Array2Array39UByte.get(index: Int): Array39UByte = Array39UByte(addr(index))
    operator fun Array2Array39UByte.set(index: Int, value: Array39UByte): Unit = run { memcpy(CPointer(addr(index)), CPointer(value.ptr), Array2Array39UByte.ELEMENT_SIZE_BYTES) }
    var Array2Array39UByte.value get() = this[0]; set(value) = run { this[0] = value }
    fun Array2Array39UByteAlloc(vararg items: Array39UByte): Array2Array39UByte = Array2Array39UByte(alloca_zero(Array2Array39UByte.TOTAL_SIZE_BYTES).ptr).also { for (n in 0 until items.size) it[n] = items[n] }
    operator fun Array2Array39UByte.plus(offset: Int): CPointer<Array39UByte> = CPointer<Array39UByte>(addr(offset))
    operator fun Array2Array39UByte.minus(offset: Int): CPointer<Array39UByte> = CPointer<Array39UByte>(addr(-offset))
    /*!inline*/ class Array39UByte(val ptr: Int) {
        companion object {
            const val NUM_ELEMENTS = 39
            const val ELEMENT_SIZE_BYTES = 1
            const val TOTAL_SIZE_BYTES = /*39*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
        }
        fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
    }
    operator fun Array39UByte.get(index: Int): UByte = lb(addr(index)).toUByte()
    operator fun Array39UByte.set(index: Int, value: UByte): Unit = run { sb(addr(index), (value).toByte()) }
    var Array39UByte.value get() = this[0]; set(value) = run { this[0] = value }
    fun Array39UByteAlloc(vararg items: UByte): Array39UByte = Array39UByte(alloca_zero(Array39UByte.TOTAL_SIZE_BYTES).ptr).also { for (n in 0 until items.size) it[n] = items[n] }
    operator fun Array39UByte.plus(offset: Int): CPointer<UByte> = CPointer<UByte>(addr(offset))
    operator fun Array39UByte.minus(offset: Int): CPointer<UByte> = CPointer<UByte>(addr(-offset))
    /*!inline*/ class Array2Array3Array15UByte(val ptr: Int) {
        companion object {
            const val NUM_ELEMENTS = 2
            const val ELEMENT_SIZE_BYTES = 45
            const val TOTAL_SIZE_BYTES = /*90*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
        }
        fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
    }
    operator fun Array2Array3Array15UByte.get(index: Int): Array3Array15UByte = Array3Array15UByte(addr(index))
    operator fun Array2Array3Array15UByte.set(index: Int, value: Array3Array15UByte): Unit = run { memcpy(CPointer(addr(index)), CPointer(value.ptr), Array2Array3Array15UByte.ELEMENT_SIZE_BYTES) }
    var Array2Array3Array15UByte.value get() = this[0]; set(value) = run { this[0] = value }
    fun Array2Array3Array15UByteAlloc(vararg items: Array3Array15UByte): Array2Array3Array15UByte = Array2Array3Array15UByte(alloca_zero(Array2Array3Array15UByte.TOTAL_SIZE_BYTES).ptr).also { for (n in 0 until items.size) it[n] = items[n] }
    operator fun Array2Array3Array15UByte.plus(offset: Int): CPointer<Array3Array15UByte> = CPointer<Array3Array15UByte>(addr(offset))
    operator fun Array2Array3Array15UByte.minus(offset: Int): CPointer<Array3Array15UByte> = CPointer<Array3Array15UByte>(addr(-offset))
    /*!inline*/ class Array3Array15UByte(val ptr: Int) {
        companion object {
            const val NUM_ELEMENTS = 3
            const val ELEMENT_SIZE_BYTES = 15
            const val TOTAL_SIZE_BYTES = /*45*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
        }
        fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
    }
    operator fun Array3Array15UByte.get(index: Int): Array15UByte = Array15UByte(addr(index))
    operator fun Array3Array15UByte.set(index: Int, value: Array15UByte): Unit = run { memcpy(CPointer(addr(index)), CPointer(value.ptr), Array3Array15UByte.ELEMENT_SIZE_BYTES) }
    var Array3Array15UByte.value get() = this[0]; set(value) = run { this[0] = value }
    fun Array3Array15UByteAlloc(vararg items: Array15UByte): Array3Array15UByte = Array3Array15UByte(alloca_zero(Array3Array15UByte.TOTAL_SIZE_BYTES).ptr).also { for (n in 0 until items.size) it[n] = items[n] }
    operator fun Array3Array15UByte.plus(offset: Int): CPointer<Array15UByte> = CPointer<Array15UByte>(addr(offset))
    operator fun Array3Array15UByte.minus(offset: Int): CPointer<Array15UByte> = CPointer<Array15UByte>(addr(-offset))
    /*!inline*/ class Array15UByte(val ptr: Int) {
        companion object {
            const val NUM_ELEMENTS = 15
            const val ELEMENT_SIZE_BYTES = 1
            const val TOTAL_SIZE_BYTES = /*15*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
        }
        fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
    }
    operator fun Array15UByte.get(index: Int): UByte = lb(addr(index)).toUByte()
    operator fun Array15UByte.set(index: Int, value: UByte): Unit = run { sb(addr(index), (value).toByte()) }
    var Array15UByte.value get() = this[0]; set(value) = run { this[0] = value }
    fun Array15UByteAlloc(vararg items: UByte): Array15UByte = Array15UByte(alloca_zero(Array15UByte.TOTAL_SIZE_BYTES).ptr).also { for (n in 0 until items.size) it[n] = items[n] }
    operator fun Array15UByte.plus(offset: Int): CPointer<UByte> = CPointer<UByte>(addr(offset))
    operator fun Array15UByte.minus(offset: Int): CPointer<UByte> = CPointer<UByte>(addr(-offset))
    /*!inline*/ class Array3UInt(val ptr: Int) {
        companion object {
            const val NUM_ELEMENTS = 3
            const val ELEMENT_SIZE_BYTES = 4
            const val TOTAL_SIZE_BYTES = /*12*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
        }
        fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
    }
    operator fun Array3UInt.get(index: Int): UInt = lw(addr(index)).toUInt()
    operator fun Array3UInt.set(index: Int, value: UInt): Unit = run { sw(addr(index), (value).toInt()) }
    var Array3UInt.value get() = this[0]; set(value) = run { this[0] = value }
    fun Array3UIntAlloc(vararg items: UInt): Array3UInt = Array3UInt(alloca_zero(Array3UInt.TOTAL_SIZE_BYTES).ptr).also { for (n in 0 until items.size) it[n] = items[n] }
    operator fun Array3UInt.plus(offset: Int): CPointer<UInt> = CPointer<UInt>(addr(offset))
    operator fun Array3UInt.minus(offset: Int): CPointer<UInt> = CPointer<UInt>(addr(-offset))
    /*!inline*/ class Array54Float(val ptr: Int) {
        companion object {
            const val NUM_ELEMENTS = 54
            const val ELEMENT_SIZE_BYTES = 4
            const val TOTAL_SIZE_BYTES = /*216*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
        }
        fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
    }
    operator fun Array54Float.get(index: Int): Float = Float.fromBits(lw(addr(index)))
    operator fun Array54Float.set(index: Int, value: Float): Unit = run { sw(addr(index), (value).toBits()) }
    var Array54Float.value get() = this[0]; set(value) = run { this[0] = value }
    fun Array54FloatAlloc(vararg items: Float): Array54Float = Array54Float(alloca_zero(Array54Float.TOTAL_SIZE_BYTES).ptr).also { for (n in 0 until items.size) it[n] = items[n] }
    operator fun Array54Float.plus(offset: Int): CPointer<Float> = CPointer<Float>(addr(offset))
    operator fun Array54Float.minus(offset: Int): CPointer<Float> = CPointer<Float>(addr(-offset))
    /*!inline*/ class Array8Array23UByte(val ptr: Int) {
        companion object {
            const val NUM_ELEMENTS = 8
            const val ELEMENT_SIZE_BYTES = 23
            const val TOTAL_SIZE_BYTES = /*184*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
        }
        fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
    }
    operator fun Array8Array23UByte.get(index: Int): Array23UByte = Array23UByte(addr(index))
    operator fun Array8Array23UByte.set(index: Int, value: Array23UByte): Unit = run { memcpy(CPointer(addr(index)), CPointer(value.ptr), Array8Array23UByte.ELEMENT_SIZE_BYTES) }
    var Array8Array23UByte.value get() = this[0]; set(value) = run { this[0] = value }
    fun Array8Array23UByteAlloc(vararg items: Array23UByte): Array8Array23UByte = Array8Array23UByte(alloca_zero(Array8Array23UByte.TOTAL_SIZE_BYTES).ptr).also { for (n in 0 until items.size) it[n] = items[n] }
    operator fun Array8Array23UByte.plus(offset: Int): CPointer<Array23UByte> = CPointer<Array23UByte>(addr(offset))
    operator fun Array8Array23UByte.minus(offset: Int): CPointer<Array23UByte> = CPointer<Array23UByte>(addr(-offset))
    /*!inline*/ class Array23UByte(val ptr: Int) {
        companion object {
            const val NUM_ELEMENTS = 23
            const val ELEMENT_SIZE_BYTES = 1
            const val TOTAL_SIZE_BYTES = /*23*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
        }
        fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
    }
    operator fun Array23UByte.get(index: Int): UByte = lb(addr(index)).toUByte()
    operator fun Array23UByte.set(index: Int, value: UByte): Unit = run { sb(addr(index), (value).toByte()) }
    var Array23UByte.value get() = this[0]; set(value) = run { this[0] = value }
    fun Array23UByteAlloc(vararg items: UByte): Array23UByte = Array23UByte(alloca_zero(Array23UByte.TOTAL_SIZE_BYTES).ptr).also { for (n in 0 until items.size) it[n] = items[n] }
    operator fun Array23UByte.plus(offset: Int): CPointer<UByte> = CPointer<UByte>(addr(offset))
    operator fun Array23UByte.minus(offset: Int): CPointer<UByte> = CPointer<UByte>(addr(-offset))
    /*!inline*/ class Array8Array40UByte(val ptr: Int) {
        companion object {
            const val NUM_ELEMENTS = 8
            const val ELEMENT_SIZE_BYTES = 40
            const val TOTAL_SIZE_BYTES = /*320*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
        }
        fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
    }
    operator fun Array8Array40UByte.get(index: Int): Array40UByte = Array40UByte(addr(index))
    operator fun Array8Array40UByte.set(index: Int, value: Array40UByte): Unit = run { memcpy(CPointer(addr(index)), CPointer(value.ptr), Array8Array40UByte.ELEMENT_SIZE_BYTES) }
    var Array8Array40UByte.value get() = this[0]; set(value) = run { this[0] = value }
    fun Array8Array40UByteAlloc(vararg items: Array40UByte): Array8Array40UByte = Array8Array40UByte(alloca_zero(Array8Array40UByte.TOTAL_SIZE_BYTES).ptr).also { for (n in 0 until items.size) it[n] = items[n] }
    operator fun Array8Array40UByte.plus(offset: Int): CPointer<Array40UByte> = CPointer<Array40UByte>(addr(offset))
    operator fun Array8Array40UByte.minus(offset: Int): CPointer<Array40UByte> = CPointer<Array40UByte>(addr(-offset))
    /*!inline*/ class Array40UByte(val ptr: Int) {
        companion object {
            const val NUM_ELEMENTS = 40
            const val ELEMENT_SIZE_BYTES = 1
            const val TOTAL_SIZE_BYTES = /*40*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
        }
        fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
    }
    operator fun Array40UByte.get(index: Int): UByte = lb(addr(index)).toUByte()
    operator fun Array40UByte.set(index: Int, value: UByte): Unit = run { sb(addr(index), (value).toByte()) }
    var Array40UByte.value get() = this[0]; set(value) = run { this[0] = value }
    fun Array40UByteAlloc(vararg items: UByte): Array40UByte = Array40UByte(alloca_zero(Array40UByte.TOTAL_SIZE_BYTES).ptr).also { for (n in 0 until items.size) it[n] = items[n] }
    operator fun Array40UByte.plus(offset: Int): CPointer<UByte> = CPointer<UByte>(addr(offset))
    operator fun Array40UByte.minus(offset: Int): CPointer<UByte> = CPointer<UByte>(addr(-offset))
    /*!inline*/ class Array4Float(val ptr: Int) {
        companion object {
            const val NUM_ELEMENTS = 4
            const val ELEMENT_SIZE_BYTES = 4
            const val TOTAL_SIZE_BYTES = /*16*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
        }
        fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
    }
    operator fun Array4Float.get(index: Int): Float = Float.fromBits(lw(addr(index)))
    operator fun Array4Float.set(index: Int, value: Float): Unit = run { sw(addr(index), (value).toBits()) }
    var Array4Float.value get() = this[0]; set(value) = run { this[0] = value }
    fun Array4FloatAlloc(vararg items: Float): Array4Float = Array4Float(alloca_zero(Array4Float.TOTAL_SIZE_BYTES).ptr).also { for (n in 0 until items.size) it[n] = items[n] }
    operator fun Array4Float.plus(offset: Int): CPointer<Float> = CPointer<Float>(addr(offset))
    operator fun Array4Float.minus(offset: Int): CPointer<Float> = CPointer<Float>(addr(-offset))
    /*!inline*/ class Array3Array28UByte(val ptr: Int) {
        companion object {
            const val NUM_ELEMENTS = 3
            const val ELEMENT_SIZE_BYTES = 28
            const val TOTAL_SIZE_BYTES = /*84*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
        }
        fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
    }
    operator fun Array3Array28UByte.get(index: Int): Array28UByte = Array28UByte(addr(index))
    operator fun Array3Array28UByte.set(index: Int, value: Array28UByte): Unit = run { memcpy(CPointer(addr(index)), CPointer(value.ptr), Array3Array28UByte.ELEMENT_SIZE_BYTES) }
    var Array3Array28UByte.value get() = this[0]; set(value) = run { this[0] = value }
    fun Array3Array28UByteAlloc(vararg items: Array28UByte): Array3Array28UByte = Array3Array28UByte(alloca_zero(Array3Array28UByte.TOTAL_SIZE_BYTES).ptr).also { for (n in 0 until items.size) it[n] = items[n] }
    operator fun Array3Array28UByte.plus(offset: Int): CPointer<Array28UByte> = CPointer<Array28UByte>(addr(offset))
    operator fun Array3Array28UByte.minus(offset: Int): CPointer<Array28UByte> = CPointer<Array28UByte>(addr(-offset))
    /*!inline*/ class Array28UByte(val ptr: Int) {
        companion object {
            const val NUM_ELEMENTS = 28
            const val ELEMENT_SIZE_BYTES = 1
            const val TOTAL_SIZE_BYTES = /*28*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
        }
        fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
    }
    operator fun Array28UByte.get(index: Int): UByte = lb(addr(index)).toUByte()
    operator fun Array28UByte.set(index: Int, value: UByte): Unit = run { sb(addr(index), (value).toByte()) }
    var Array28UByte.value get() = this[0]; set(value) = run { this[0] = value }
    fun Array28UByteAlloc(vararg items: UByte): Array28UByte = Array28UByte(alloca_zero(Array28UByte.TOTAL_SIZE_BYTES).ptr).also { for (n in 0 until items.size) it[n] = items[n] }
    operator fun Array28UByte.plus(offset: Int): CPointer<UByte> = CPointer<UByte>(addr(offset))
    operator fun Array28UByte.minus(offset: Int): CPointer<UByte> = CPointer<UByte>(addr(-offset))
    /*!inline*/ class Array16UByte(val ptr: Int) {
        companion object {
            const val NUM_ELEMENTS = 16
            const val ELEMENT_SIZE_BYTES = 1
            const val TOTAL_SIZE_BYTES = /*16*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
        }
        fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
    }
    operator fun Array16UByte.get(index: Int): UByte = lb(addr(index)).toUByte()
    operator fun Array16UByte.set(index: Int, value: UByte): Unit = run { sb(addr(index), (value).toByte()) }
    var Array16UByte.value get() = this[0]; set(value) = run { this[0] = value }
    fun Array16UByteAlloc(vararg items: UByte): Array16UByte = Array16UByte(alloca_zero(Array16UByte.TOTAL_SIZE_BYTES).ptr).also { for (n in 0 until items.size) it[n] = items[n] }
    operator fun Array16UByte.plus(offset: Int): CPointer<UByte> = CPointer<UByte>(addr(offset))
    operator fun Array16UByte.minus(offset: Int): CPointer<UByte> = CPointer<UByte>(addr(-offset))
    /*!inline*/ class Array24UByte(val ptr: Int) {
        companion object {
            const val NUM_ELEMENTS = 24
            const val ELEMENT_SIZE_BYTES = 1
            const val TOTAL_SIZE_BYTES = /*24*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
        }
        fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
    }
    operator fun Array24UByte.get(index: Int): UByte = lb(addr(index)).toUByte()
    operator fun Array24UByte.set(index: Int, value: UByte): Unit = run { sb(addr(index), (value).toByte()) }
    var Array24UByte.value get() = this[0]; set(value) = run { this[0] = value }
    fun Array24UByteAlloc(vararg items: UByte): Array24UByte = Array24UByte(alloca_zero(Array24UByte.TOTAL_SIZE_BYTES).ptr).also { for (n in 0 until items.size) it[n] = items[n] }
    operator fun Array24UByte.plus(offset: Int): CPointer<UByte> = CPointer<UByte>(addr(offset))
    operator fun Array24UByte.minus(offset: Int): CPointer<UByte> = CPointer<UByte>(addr(-offset))
    /*!inline*/ class Array10UByte(val ptr: Int) {
        companion object {
            const val NUM_ELEMENTS = 10
            const val ELEMENT_SIZE_BYTES = 1
            const val TOTAL_SIZE_BYTES = /*10*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
        }
        fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
    }
    operator fun Array10UByte.get(index: Int): UByte = lb(addr(index)).toUByte()
    operator fun Array10UByte.set(index: Int, value: UByte): Unit = run { sb(addr(index), (value).toByte()) }
    var Array10UByte.value get() = this[0]; set(value) = run { this[0] = value }
    fun Array10UByteAlloc(vararg items: UByte): Array10UByte = Array10UByte(alloca_zero(Array10UByte.TOTAL_SIZE_BYTES).ptr).also { for (n in 0 until items.size) it[n] = items[n] }
    operator fun Array10UByte.plus(offset: Int): CPointer<UByte> = CPointer<UByte>(addr(offset))
    operator fun Array10UByte.minus(offset: Int): CPointer<UByte> = CPointer<UByte>(addr(-offset))
    /*!inline*/ class Array145Float(val ptr: Int) {
        companion object {
            const val NUM_ELEMENTS = 145
            const val ELEMENT_SIZE_BYTES = 4
            const val TOTAL_SIZE_BYTES = /*580*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
        }
        fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
    }
    operator fun Array145Float.get(index: Int): Float = Float.fromBits(lw(addr(index)))
    operator fun Array145Float.set(index: Int, value: Float): Unit = run { sw(addr(index), (value).toBits()) }
    var Array145Float.value get() = this[0]; set(value) = run { this[0] = value }
    fun Array145FloatAlloc(vararg items: Float): Array145Float = Array145Float(alloca_zero(Array145Float.TOTAL_SIZE_BYTES).ptr).also { for (n in 0 until items.size) it[n] = items[n] }
    operator fun Array145Float.plus(offset: Int): CPointer<Float> = CPointer<Float>(addr(offset))
    operator fun Array145Float.minus(offset: Int): CPointer<Float> = CPointer<Float>(addr(-offset))
    /*!inline*/ class Array32Short(val ptr: Int) {
        companion object {
            const val NUM_ELEMENTS = 32
            const val ELEMENT_SIZE_BYTES = 2
            const val TOTAL_SIZE_BYTES = /*64*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
        }
        fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
    }
    operator fun Array32Short.get(index: Int): Short = lh(addr(index))
    operator fun Array32Short.set(index: Int, value: Short): Unit = run { sh(addr(index), value) }
    var Array32Short.value get() = this[0]; set(value) = run { this[0] = value }
    fun Array32ShortAlloc(vararg items: Short): Array32Short = Array32Short(alloca_zero(Array32Short.TOTAL_SIZE_BYTES).ptr).also { for (n in 0 until items.size) it[n] = items[n] }
    operator fun Array32Short.plus(offset: Int): CPointer<Short> = CPointer<Short>(addr(offset))
    operator fun Array32Short.minus(offset: Int): CPointer<Short> = CPointer<Short>(addr(-offset))
    /*!inline*/ class Array3Int(val ptr: Int) {
        companion object {
            const val NUM_ELEMENTS = 3
            const val ELEMENT_SIZE_BYTES = 4
            const val TOTAL_SIZE_BYTES = /*12*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
        }
        fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
    }
    operator fun Array3Int.get(index: Int): Int = lw(addr(index))
    operator fun Array3Int.set(index: Int, value: Int): Unit = run { sw(addr(index), value) }
    var Array3Int.value get() = this[0]; set(value) = run { this[0] = value }
    fun Array3IntAlloc(vararg items: Int): Array3Int = Array3Int(alloca_zero(Array3Int.TOTAL_SIZE_BYTES).ptr).also { for (n in 0 until items.size) it[n] = items[n] }
    operator fun Array3Int.plus(offset: Int): CPointer<Int> = CPointer<Int>(addr(offset))
    operator fun Array3Int.minus(offset: Int): CPointer<Int> = CPointer<Int>(addr(-offset))
    /*!inline*/ class Array14Float(val ptr: Int) {
        companion object {
            const val NUM_ELEMENTS = 14
            const val ELEMENT_SIZE_BYTES = 4
            const val TOTAL_SIZE_BYTES = /*56*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
        }
        fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
    }
    operator fun Array14Float.get(index: Int): Float = Float.fromBits(lw(addr(index)))
    operator fun Array14Float.set(index: Int, value: Float): Unit = run { sw(addr(index), (value).toBits()) }
    var Array14Float.value get() = this[0]; set(value) = run { this[0] = value }
    fun Array14FloatAlloc(vararg items: Float): Array14Float = Array14Float(alloca_zero(Array14Float.TOTAL_SIZE_BYTES).ptr).also { for (n in 0 until items.size) it[n] = items[n] }
    operator fun Array14Float.plus(offset: Int): CPointer<Float> = CPointer<Float>(addr(offset))
    operator fun Array14Float.minus(offset: Int): CPointer<Float> = CPointer<Float>(addr(-offset))
    /*!inline*/ class Array2Array8Float(val ptr: Int) {
        companion object {
            const val NUM_ELEMENTS = 2
            const val ELEMENT_SIZE_BYTES = 32
            const val TOTAL_SIZE_BYTES = /*64*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
        }
        fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
    }
    operator fun Array2Array8Float.get(index: Int): Array8Float = Array8Float(addr(index))
    operator fun Array2Array8Float.set(index: Int, value: Array8Float): Unit = run { memcpy(CPointer(addr(index)), CPointer(value.ptr), Array2Array8Float.ELEMENT_SIZE_BYTES) }
    var Array2Array8Float.value get() = this[0]; set(value) = run { this[0] = value }
    fun Array2Array8FloatAlloc(vararg items: Array8Float): Array2Array8Float = Array2Array8Float(alloca_zero(Array2Array8Float.TOTAL_SIZE_BYTES).ptr).also { for (n in 0 until items.size) it[n] = items[n] }
    operator fun Array2Array8Float.plus(offset: Int): CPointer<Array8Float> = CPointer<Array8Float>(addr(offset))
    operator fun Array2Array8Float.minus(offset: Int): CPointer<Array8Float> = CPointer<Array8Float>(addr(-offset))
    /*!inline*/ class Array8Float(val ptr: Int) {
        companion object {
            const val NUM_ELEMENTS = 8
            const val ELEMENT_SIZE_BYTES = 4
            const val TOTAL_SIZE_BYTES = /*32*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
        }
        fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
    }
    operator fun Array8Float.get(index: Int): Float = Float.fromBits(lw(addr(index)))
    operator fun Array8Float.set(index: Int, value: Float): Unit = run { sw(addr(index), (value).toBits()) }
    var Array8Float.value get() = this[0]; set(value) = run { this[0] = value }
    fun Array8FloatAlloc(vararg items: Float): Array8Float = Array8Float(alloca_zero(Array8Float.TOTAL_SIZE_BYTES).ptr).also { for (n in 0 until items.size) it[n] = items[n] }
    operator fun Array8Float.plus(offset: Int): CPointer<Float> = CPointer<Float>(addr(offset))
    operator fun Array8Float.minus(offset: Int): CPointer<Float> = CPointer<Float>(addr(-offset))
    /*!inline*/ class Array18Float(val ptr: Int) {
        companion object {
            const val NUM_ELEMENTS = 18
            const val ELEMENT_SIZE_BYTES = 4
            const val TOTAL_SIZE_BYTES = /*72*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
        }
        fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
    }
    operator fun Array18Float.get(index: Int): Float = Float.fromBits(lw(addr(index)))
    operator fun Array18Float.set(index: Int, value: Float): Unit = run { sw(addr(index), (value).toBits()) }
    var Array18Float.value get() = this[0]; set(value) = run { this[0] = value }
    fun Array18FloatAlloc(vararg items: Float): Array18Float = Array18Float(alloca_zero(Array18Float.TOTAL_SIZE_BYTES).ptr).also { for (n in 0 until items.size) it[n] = items[n] }
    operator fun Array18Float.plus(offset: Int): CPointer<Float> = CPointer<Float>(addr(offset))
    operator fun Array18Float.minus(offset: Int): CPointer<Float> = CPointer<Float>(addr(-offset))
    /*!inline*/ class Array9Float(val ptr: Int) {
        companion object {
            const val NUM_ELEMENTS = 9
            const val ELEMENT_SIZE_BYTES = 4
            const val TOTAL_SIZE_BYTES = /*36*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
        }
        fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
    }
    operator fun Array9Float.get(index: Int): Float = Float.fromBits(lw(addr(index)))
    operator fun Array9Float.set(index: Int, value: Float): Unit = run { sw(addr(index), (value).toBits()) }
    var Array9Float.value get() = this[0]; set(value) = run { this[0] = value }
    fun Array9FloatAlloc(vararg items: Float): Array9Float = Array9Float(alloca_zero(Array9Float.TOTAL_SIZE_BYTES).ptr).also { for (n in 0 until items.size) it[n] = items[n] }
    operator fun Array9Float.plus(offset: Int): CPointer<Float> = CPointer<Float>(addr(offset))
    operator fun Array9Float.minus(offset: Int): CPointer<Float> = CPointer<Float>(addr(-offset))
    /*!inline*/ class Array6Float(val ptr: Int) {
        companion object {
            const val NUM_ELEMENTS = 6
            const val ELEMENT_SIZE_BYTES = 4
            const val TOTAL_SIZE_BYTES = /*24*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
        }
        fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
    }
    operator fun Array6Float.get(index: Int): Float = Float.fromBits(lw(addr(index)))
    operator fun Array6Float.set(index: Int, value: Float): Unit = run { sw(addr(index), (value).toBits()) }
    var Array6Float.value get() = this[0]; set(value) = run { this[0] = value }
    fun Array6FloatAlloc(vararg items: Float): Array6Float = Array6Float(alloca_zero(Array6Float.TOTAL_SIZE_BYTES).ptr).also { for (n in 0 until items.size) it[n] = items[n] }
    operator fun Array6Float.plus(offset: Int): CPointer<Float> = CPointer<Float>(addr(offset))
    operator fun Array6Float.minus(offset: Int): CPointer<Float> = CPointer<Float>(addr(-offset))
    /*!inline*/ class Array3Float(val ptr: Int) {
        companion object {
            const val NUM_ELEMENTS = 3
            const val ELEMENT_SIZE_BYTES = 4
            const val TOTAL_SIZE_BYTES = /*12*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
        }
        fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
    }
    operator fun Array3Float.get(index: Int): Float = Float.fromBits(lw(addr(index)))
    operator fun Array3Float.set(index: Int, value: Float): Unit = run { sw(addr(index), (value).toBits()) }
    var Array3Float.value get() = this[0]; set(value) = run { this[0] = value }
    fun Array3FloatAlloc(vararg items: Float): Array3Float = Array3Float(alloca_zero(Array3Float.TOTAL_SIZE_BYTES).ptr).also { for (n in 0 until items.size) it[n] = items[n] }
    operator fun Array3Float.plus(offset: Int): CPointer<Float> = CPointer<Float>(addr(offset))
    operator fun Array3Float.minus(offset: Int): CPointer<Float> = CPointer<Float>(addr(-offset))
    /*!inline*/ class Array2Array18Float(val ptr: Int) {
        companion object {
            const val NUM_ELEMENTS = 2
            const val ELEMENT_SIZE_BYTES = 72
            const val TOTAL_SIZE_BYTES = /*144*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
        }
        fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
    }
    operator fun Array2Array18Float.get(index: Int): Array18Float = Array18Float(addr(index))
    operator fun Array2Array18Float.set(index: Int, value: Array18Float): Unit = run { memcpy(CPointer(addr(index)), CPointer(value.ptr), Array2Array18Float.ELEMENT_SIZE_BYTES) }
    var Array2Array18Float.value get() = this[0]; set(value) = run { this[0] = value }
    fun Array2Array18FloatAlloc(vararg items: Array18Float): Array2Array18Float = Array2Array18Float(alloca_zero(Array2Array18Float.TOTAL_SIZE_BYTES).ptr).also { for (n in 0 until items.size) it[n] = items[n] }
    operator fun Array2Array18Float.plus(offset: Int): CPointer<Array18Float> = CPointer<Array18Float>(addr(offset))
    operator fun Array2Array18Float.minus(offset: Int): CPointer<Array18Float> = CPointer<Array18Float>(addr(-offset))
    /*!inline*/ class Array24Float(val ptr: Int) {
        companion object {
            const val NUM_ELEMENTS = 24
            const val ELEMENT_SIZE_BYTES = 4
            const val TOTAL_SIZE_BYTES = /*96*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
        }
        fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
    }
    operator fun Array24Float.get(index: Int): Float = Float.fromBits(lw(addr(index)))
    operator fun Array24Float.set(index: Int, value: Float): Unit = run { sw(addr(index), (value).toBits()) }
    var Array24Float.value get() = this[0]; set(value) = run { this[0] = value }
    fun Array24FloatAlloc(vararg items: Float): Array24Float = Array24Float(alloca_zero(Array24Float.TOTAL_SIZE_BYTES).ptr).also { for (n in 0 until items.size) it[n] = items[n] }
    operator fun Array24Float.plus(offset: Int): CPointer<Float> = CPointer<Float>(addr(offset))
    operator fun Array24Float.minus(offset: Int): CPointer<Float> = CPointer<Float>(addr(-offset))
    /*!inline*/ class Array4Array8Float(val ptr: Int) {
        companion object {
            const val NUM_ELEMENTS = 4
            const val ELEMENT_SIZE_BYTES = 32
            const val TOTAL_SIZE_BYTES = /*128*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
        }
        fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
    }
    operator fun Array4Array8Float.get(index: Int): Array8Float = Array8Float(addr(index))
    operator fun Array4Array8Float.set(index: Int, value: Array8Float): Unit = run { memcpy(CPointer(addr(index)), CPointer(value.ptr), Array4Array8Float.ELEMENT_SIZE_BYTES) }
    var Array4Array8Float.value get() = this[0]; set(value) = run { this[0] = value }
    fun Array4Array8FloatAlloc(vararg items: Array8Float): Array4Array8Float = Array4Array8Float(alloca_zero(Array4Array8Float.TOTAL_SIZE_BYTES).ptr).also { for (n in 0 until items.size) it[n] = items[n] }
    operator fun Array4Array8Float.plus(offset: Int): CPointer<Array8Float> = CPointer<Array8Float>(addr(offset))
    operator fun Array4Array8Float.minus(offset: Int): CPointer<Array8Float> = CPointer<Array8Float>(addr(-offset))
    /*!inline*/ class Array1bs_t(val ptr: Int) {
        companion object {
            const val NUM_ELEMENTS = 1
            const val ELEMENT_SIZE_BYTES = 12
            const val TOTAL_SIZE_BYTES = /*12*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
        }
        fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
    }
    operator fun Array1bs_t.get(index: Int): bs_t = bs_t(addr(index))
    operator fun Array1bs_t.set(index: Int, value: bs_t): Unit = run { bs_t(addr(index)).copyFrom(value) }
    var Array1bs_t.value get() = this[0]; set(value) = run { this[0] = value }
    fun Array1bs_tAlloc(vararg items: bs_t): Array1bs_t = Array1bs_t(alloca_zero(Array1bs_t.TOTAL_SIZE_BYTES).ptr).also { for (n in 0 until items.size) it[n] = items[n] }
    operator fun Array1bs_t.plus(offset: Int): CPointer<bs_t> = CPointer<bs_t>(addr(offset))
    operator fun Array1bs_t.minus(offset: Int): CPointer<bs_t> = CPointer<bs_t>(addr(-offset))
    /*!inline*/ class Array1L12_scale_info(val ptr: Int) {
        companion object {
            const val NUM_ELEMENTS = 1
            const val ELEMENT_SIZE_BYTES = 898
            const val TOTAL_SIZE_BYTES = /*898*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
        }
        fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
    }
    operator fun Array1L12_scale_info.get(index: Int): L12_scale_info = L12_scale_info(addr(index))
    operator fun Array1L12_scale_info.set(index: Int, value: L12_scale_info): Unit = run { L12_scale_info(addr(index)).copyFrom(value) }
    var Array1L12_scale_info.value get() = this[0]; set(value) = run { this[0] = value }
    fun Array1L12_scale_infoAlloc(vararg items: L12_scale_info): Array1L12_scale_info = Array1L12_scale_info(alloca_zero(Array1L12_scale_info.TOTAL_SIZE_BYTES).ptr).also { for (n in 0 until items.size) it[n] = items[n] }
    operator fun Array1L12_scale_info.plus(offset: Int): CPointer<L12_scale_info> = CPointer<L12_scale_info>(addr(offset))
    operator fun Array1L12_scale_info.minus(offset: Int): CPointer<L12_scale_info> = CPointer<L12_scale_info>(addr(-offset))
}
// KTCC RUNTIME ///////////////////////////////////////////////////
/*!!inline*/ data class CPointer<T>(val ptr: Int)
/*!!inline*/ data class CFunction0<TR>(val ptr: Int)
/*!!inline*/ data class CFunction1<T0, TR>(val ptr: Int)
/*!!inline*/ data class CFunction2<T0, T1, TR>(val ptr: Int)
/*!!inline*/ data class CFunction3<T0, T1, T2, TR>(val ptr: Int)
/*!!inline*/ data class CFunction4<T0, T1, T2, T3, TR>(val ptr: Int)
/*!!inline*/ data class CFunction5<T0, T1, T2, T3, T4, TR>(val ptr: Int)
/*!!inline*/ data class CFunction6<T0, T1, T2, T3, T4, T5, TR>(val ptr: Int)
/*!!inline*/ data class CFunction7<T0, T1, T2, T3, T4, T5, T6, TR>(val ptr: Int)

@Suppress("MemberVisibilityCanBePrivate", "FunctionName", "CanBeVal", "DoubleNegation", "LocalVariableName", "NAME_SHADOWING", "VARIABLE_WITH_REDUNDANT_INITIALIZER", "RemoveRedundantCallsOfConversionMethods", "EXPERIMENTAL_IS_NOT_ENABLED", "RedundantExplicitType", "RemoveExplicitTypeArguments", "RedundantExplicitType", "unused", "UNCHECKED_CAST", "UNUSED_VARIABLE", "UNUSED_PARAMETER", "NOTHING_TO_INLINE", "PropertyName", "ClassName", "USELESS_CAST", "PrivatePropertyName", "CanBeParameter", "UnusedMainParameter")
@UseExperimental(ExperimentalUnsignedTypes::class)
open class Runtime(val REQUESTED_HEAP_SIZE: Int = 0) {
    val Float.Companion.SIZE_BYTES get() = 4
    val Double.Companion.SIZE_BYTES get() = 8

    infix fun UByte.shr(other: Int): UInt = this.toUInt() shr other
    infix fun UByte.shl(other: Int): UInt = this.toUInt() shl other

    val HEAP_SIZE = if (REQUESTED_HEAP_SIZE <= 0) 16 * 1024 * 1024 else REQUESTED_HEAP_SIZE // 16 MB default
    val HEAP: java.nio.ByteBuffer = java.nio.ByteBuffer.allocateDirect(HEAP_SIZE).order(java.nio.ByteOrder.LITTLE_ENDIAN)

    val FUNCTIONS = arrayListOf<kotlin.reflect.KFunction<*>>()

    interface IStruct {
    }

    interface IStructCompanion<T : IStruct> {
        val SIZE: Int
    }

    val POINTER_SIZE = 4

    var STACK_PTR = 512 * 1024 // 0.5 MB
    var HEAP_PTR = STACK_PTR

    fun lb(ptr: Int) = HEAP[ptr]
    fun sb(ptr: Int, value: Byte): Unit = run { HEAP.put(ptr, value) }

    fun lh(ptr: Int): Short = HEAP.getShort(ptr)
    fun sh(ptr: Int, value: Short): Unit = run { HEAP.putShort(ptr, value) }

    fun lw(ptr: Int): Int = HEAP.getInt(ptr)
    fun sw(ptr: Int, value: Int): Unit = run { HEAP.putInt(ptr, value) }

    fun ld(ptr: Int): Long = HEAP.getLong(ptr)
    fun sd(ptr: Int, value: Long): Unit = run { HEAP.putLong(ptr, value) }

    inline fun <T> Int.toCPointer(): CPointer<T> = CPointer(this)
    inline fun <T> CPointer<*>.toCPointer(): CPointer<T> = CPointer(this.ptr)

    fun <T> CPointer<T>.addPtr(offset: Int, elementSize: Int) = CPointer<T>(this.ptr + offset * elementSize)

    @JvmName("plusPtr") operator fun <T> CPointer<CPointer<T>>.plus(offset: Int) = addPtr<CPointer<T>>(offset, 4)
    @JvmName("minusPtr") operator fun <T> CPointer<CPointer<T>>.minus(offset: Int) = addPtr<CPointer<T>>(-offset, 4)
    @JvmName("minusPtr") operator fun <T> CPointer<CPointer<T>>.minus(other: CPointer<CPointer<T>>) = (this.ptr - other.ptr) / 4

    operator fun <T> CPointer<CPointer<T>>.set(offset: Int, value: CPointer<T>) = sw(this.ptr + offset * 4, value.ptr)
    operator fun <T> CPointer<CPointer<T>>.get(offset: Int): CPointer<T> = CPointer(lw(this.ptr + offset * 4))

    var <T> CPointer<CPointer<T>>.value: CPointer<T> get() = this[0]; set(value) = run { this[0] = value }

    fun Boolean.toInt() = if (this) 1 else 0
    fun CPointer<*>.toInt() = ptr
    fun CPointer<*>.toBool() = ptr != 0

    inline fun Number.toBool() = this.toInt() != 0
    inline fun UByte.toBool() = this.toInt() != 0
    inline fun UShort.toBool() = this.toInt() != 0
    inline fun UInt.toBool() = this.toInt() != 0
    inline fun ULong.toBool() = this.toInt() != 0
    fun Boolean.toBool() = this

    // STACK ALLOC
    inline fun <T> stackFrame(callback: () -> T): T {
        val oldPos = STACK_PTR
        return try { callback() } finally { STACK_PTR = oldPos }
    }
    fun alloca(size: Int): CPointer<Unit> = CPointer<Unit>((STACK_PTR - size).also { STACK_PTR -= size })
    fun alloca_zero(size: Int): CPointer<Unit> = alloca(size).also { memset(it, 0, size) }

    data class Chunk(val head: Int, val size: Int)

    val chunks = LinkedHashMap<Int, Chunk>()
    val freeChunks = arrayListOf<Chunk>()

    // HEAP ALLOC
    // @TODO: OPTIMIZE!
    fun malloc(size: Int): CPointer<Unit> {
        val chunk = freeChunks.firstOrNull { it.size >= size }
        if (chunk != null) {
            freeChunks.remove(chunk)
            chunks[chunk.head] = chunk
            return CPointer(chunk.head)
        } else {
            val head = HEAP_PTR
            HEAP_PTR += size
            chunks[head] = Chunk(head, size)
            return CPointer(head)
        }
    }
    fun free(ptr: CPointer<*>): Unit {
        chunks.remove(ptr.ptr)?.let {
            freeChunks += it
        }
    }

    // I/O
    fun putchar(c: Int): Int = c.also { System.out.print(c.toChar()) }

    val fileHandlers = LinkedHashMap<Int, java.io.RandomAccessFile>()
    var lastFileHandle = 1

    fun exit(code: Int) {
        kotlin.system.exitProcess(code)
    }

    fun fopen(file: CPointer<Byte>, mode: CPointer<Byte>): CPointer<CPointer<Unit>> {
        return try {
            val modep = mode.readStringz().replace("b", "")
            val modeAppend = modep.contains("a")
            val modeWrite = modep.contains("w") || modeAppend
            val raf = java.io.RandomAccessFile(file.readStringz(), when {
                modeWrite -> "rw"
                else -> "r"
            })
            if (modeAppend) {
                raf.seek(raf.length())
            }
            val fileHandle = lastFileHandle++
            fileHandlers[fileHandle] = raf
            CPointer<CPointer<Unit>>(fileHandle)
        } catch (e: java.io.FileNotFoundException) {
            CPointer<CPointer<Unit>>(0)
        }
    }

    fun prevAligned(size: Int, alignment: Int): Int {
        if (size % alignment == 0) return size
        return size - (alignment - (size % alignment))
    }

    fun memWrite(ptr: CPointer<*>, data: ByteArray, offset: Int = 0, size: Int = data.size) = run { for (n in offset until offset + size) sb(ptr.ptr + n, data[n]) }
    fun memRead(ptr: CPointer<*>, data: ByteArray, offset: Int = 0, size: Int = data.size) = run { for (n in offset until offset + size) data[n] = lb(ptr.ptr + n) }

    fun memWrite(ptr: CPointer<*>, data: ShortArray, offset: Int = 0, size: Int = data.size) = run { for (n in offset until offset + size) sh(ptr.ptr + n * 2, data[n]) }
    fun memRead(ptr: CPointer<*>, data: ShortArray, offset: Int = 0, size: Int = data.size) = run { for (n in offset until offset + size) data[n] = lh(ptr.ptr + n * 2) }


    fun fread(ptr: CPointer<Unit>, size: Int, nmemb: Int, stream: CPointer<CPointer<Unit>>): Int {
        val raf = fileHandlers[stream.ptr] ?: return -1
        val available = raf.length() - raf.filePointer
        val temp = ByteArray(prevAligned(kotlin.math.min(available.toLong(), (size * nmemb).toLong()).toInt(), size))
        val readCount = raf.read(temp)
        memWrite(ptr, temp, 0, readCount)
        return readCount / size
    }

    fun fwrite(ptr: CPointer<Unit>, size: Int, nmemb: Int, stream: CPointer<CPointer<Unit>>): Int {
        val raf = fileHandlers[stream.ptr] ?: return -1
        val temp = ByteArray(size * nmemb)
        memRead(ptr, temp)
        raf.write(temp)
        return nmemb
    }

    fun fflush(stream: CPointer<CPointer<Unit>>): Int {
        val raf = fileHandlers[stream.ptr] ?: return -1
        return 0
    }

    fun ftell(stream: CPointer<CPointer<Unit>>): Long {
        val raf = fileHandlers[stream.ptr] ?: return 0L
        return raf.filePointer
    }

    fun fsetpos(stream: CPointer<CPointer<Unit>>, ptrHolder: CPointer<Long>): Int {
        val raf = fileHandlers[stream.ptr] ?: return -1
        raf.seek(ptrHolder[0])
        return 0
    }

    fun fgetpos(stream: CPointer<CPointer<Unit>>, ptrHolder: CPointer<Long>): Int {
        val raf = fileHandlers[stream.ptr] ?: return -1
        ptrHolder[0] = raf.filePointer
        return 0
    }

    fun fseek(stream: CPointer<CPointer<Unit>>, offset: Long, whence: Int): Int {
        val raf = fileHandlers[stream.ptr] ?: return -1
        when (whence) {
            0 -> raf.seek(offset)
            1 -> raf.seek(raf.filePointer + offset)
            2 -> raf.seek(raf.length() + offset)
        }
        return 0
    }

    fun fclose(stream: CPointer<CPointer<Unit>>) {
        val raf = fileHandlers.remove(stream.ptr)
        raf?.close()
    }

    fun Any?.getAsInt() = when (this) {
        null -> 0
        is CPointer<*> -> this.ptr
        is UByte -> this.toInt()
        is UShort -> this.toInt()
        is UInt -> this.toInt()
        is Number -> this.toInt()
        else -> -1
    }

    fun Any?.getAsString() = when (this) {
        null -> "NULL"
        is CPointer<*> -> (this as CPointer<Byte>).readStringz()
        is Number -> CPointer<Byte>(this.toInt()).readStringz()
        else -> "<INVALID>"
    }

    fun printf(format: CPointer<Byte>, vararg params: Any?) {
        var paramPos = 0
        val fmt = format.readStringz()
        var n = 0
        while (n < fmt.length) {
            val c = fmt[n++]
            if (c == '%') {
                var c2: Char = ' '
                var pad: Char = ' '
                var len: Int = 0
                do {
                    c2 = fmt[n++]
                    if (c2 == '0') {
                        pad = c2
                    } else if (c2 in '0'..'9') {
                        len *= 10
                        len += c2 - '0'
                    }
                } while (c2 in '0'..'9')
                val vparam = params.getOrNull(paramPos++)
                //println("VPARAM: $vparam : ${vparam!!::class}")
                val iparam = vparam?.getAsInt() ?: 0
                when (c2) {
                    'p' -> System.out.printf("0x%08x", iparam)
                    'f' -> System.out.printf("%f", (vparam as Number).toFloat())
                    'd' -> print(iparam.toString(10).padStart(len, pad))
                    'x' -> print((iparam.toLong() and 0xFFFFFFFFL).toString(16).toLowerCase().padStart(len, pad))
                    'X' -> print((iparam.toLong() and 0xFFFFFFFFL).toString(16).toUpperCase().padStart(len, pad))
                    's' -> print(CPointer<Byte>(iparam).getAsString())
                    else -> {
                        print(c)
                        print(c2)
                    }
                }
            } else {
                putchar(c.toInt())
            }
        }
    }

    // string/memory
    fun memset(ptr: CPointer<*>, value: Int, num: Int): CPointer<Unit> = run { for (n in 0 until num) { sb(ptr.ptr + n, value.toByte()) }; return (ptr as CPointer<Unit>) }
    fun memcpy(dest: CPointer<Unit>, src: CPointer<Unit>, num: Int): CPointer<Unit> {
        for (n in 0 until num) {
            sb(dest.ptr + n, lb(src.ptr + n))
        }
        return dest as CPointer<Unit>
    }
    fun memmove(dest: CPointer<Unit>, src: CPointer<Unit>, num: Int): CPointer<Unit> {
        if (dest.ptr > src.ptr) {
            for (m in 0 until num) {
                val n = num - 1 - m
                sb(dest.ptr + n, lb(src.ptr + n))
            }
        } else {
            for (n in 0 until num) {
                sb(dest.ptr + n, lb(src.ptr + n))
            }
        }
        return dest as CPointer<Unit>
    }

    private val STRINGS = LinkedHashMap<String, CPointer<Byte>>()

    // @TODO: UTF-8?
    fun CPointer<Byte>.readStringz(): String {
        var sb = StringBuilder()
        var pos = this.ptr
        while (true) {
            val c = lb(pos++)
            if (c == 0.toByte()) break
            sb.append(c.toChar())
        }
        return sb.toString()
    }

    val String.ptr: CPointer<Byte> get() = STRINGS.getOrPut(this) {
        val bytes = this.toByteArray(Charsets.UTF_8)
        val ptr = malloc(bytes.size + 1).toCPointer<Byte>()
        val p = ptr.ptr
        for (n in 0 until bytes.size) sb(p + n, bytes[n])
        sb(p + bytes.size, 0)
        ptr
    }

    val Array<String>.ptr: CPointer<CPointer<Byte>> get() {
        val array = this
        val ptr = malloc(POINTER_SIZE * array.size).toCPointer<CPointer<Byte>>()
        for (n in 0 until array.size) {
            sw(ptr.ptr + n * POINTER_SIZE, array[n].ptr.ptr)
        }
        return ptr
    }

    @JvmName("getterByte") operator fun CPointer<Byte>.get(offset: Int): Byte = lb(this.ptr + offset * 1)
    @JvmName("setterByte") operator fun CPointer<Byte>.set(offset: Int, value: Byte) = sb(this.ptr + offset * 1, value)
    @set:JvmName("setter_Byte_value") @get:JvmName("getter_Byte_value") var CPointer<Byte>.value: Byte get() = this[0]; set(value): Unit = run { this[0] = value }
    @JvmName("plusByte") operator fun CPointer<Byte>.plus(offset: Int) = addPtr<Byte>(offset, 1)
    @JvmName("minusByte") operator fun CPointer<Byte>.minus(offset: Int) = addPtr<Byte>(-offset, 1)
    @JvmName("minusBytePtr") operator fun CPointer<Byte>.minus(other: CPointer<Byte>) = (this.ptr - other.ptr) / 1
    fun fixedArrayOfByte(size: Int, vararg values: Byte): CPointer<Byte> = alloca_zero(size * 1).toCPointer<Byte>().also { for (n in 0 until values.size) sb(it.ptr + n * 1, values[n]) }

    @JvmName("getterShort") operator fun CPointer<Short>.get(offset: Int): Short = lh(this.ptr + offset * 2)
    @JvmName("setterShort") operator fun CPointer<Short>.set(offset: Int, value: Short) = sh(this.ptr + offset * 2, value)
    @set:JvmName("setter_Short_value") @get:JvmName("getter_Short_value") var CPointer<Short>.value: Short get() = this[0]; set(value): Unit = run { this[0] = value }
    @JvmName("plusShort") operator fun CPointer<Short>.plus(offset: Int) = addPtr<Short>(offset, 2)
    @JvmName("minusShort") operator fun CPointer<Short>.minus(offset: Int) = addPtr<Short>(-offset, 2)
    @JvmName("minusShortPtr") operator fun CPointer<Short>.minus(other: CPointer<Short>) = (this.ptr - other.ptr) / 2
    fun fixedArrayOfShort(size: Int, vararg values: Short): CPointer<Short> = alloca_zero(size * 2).toCPointer<Short>().also { for (n in 0 until values.size) sh(it.ptr + n * 2, values[n]) }

    @JvmName("getterInt") operator fun CPointer<Int>.get(offset: Int): Int = lw(this.ptr + offset * 4)
    @JvmName("setterInt") operator fun CPointer<Int>.set(offset: Int, value: Int) = sw(this.ptr + offset * 4, value)
    @set:JvmName("setter_Int_value") @get:JvmName("getter_Int_value") var CPointer<Int>.value: Int get() = this[0]; set(value): Unit = run { this[0] = value }
    @JvmName("plusInt") operator fun CPointer<Int>.plus(offset: Int) = addPtr<Int>(offset, 4)
    @JvmName("minusInt") operator fun CPointer<Int>.minus(offset: Int) = addPtr<Int>(-offset, 4)
    @JvmName("minusIntPtr") operator fun CPointer<Int>.minus(other: CPointer<Int>) = (this.ptr - other.ptr) / 4
    fun fixedArrayOfInt(size: Int, vararg values: Int): CPointer<Int> = alloca_zero(size * 4).toCPointer<Int>().also { for (n in 0 until values.size) sw(it.ptr + n * 4, values[n]) }

    @JvmName("getterLong") operator fun CPointer<Long>.get(offset: Int): Long = ld(this.ptr + offset * 8)
    @JvmName("setterLong") operator fun CPointer<Long>.set(offset: Int, value: Long) = sd(this.ptr + offset * 8, value)
    @set:JvmName("setter_Long_value") @get:JvmName("getter_Long_value") var CPointer<Long>.value: Long get() = this[0]; set(value): Unit = run { this[0] = value }
    @JvmName("plusLong") operator fun CPointer<Long>.plus(offset: Int) = addPtr<Long>(offset, 8)
    @JvmName("minusLong") operator fun CPointer<Long>.minus(offset: Int) = addPtr<Long>(-offset, 8)
    @JvmName("minusLongPtr") operator fun CPointer<Long>.minus(other: CPointer<Long>) = (this.ptr - other.ptr) / 8
    fun fixedArrayOfLong(size: Int, vararg values: Long): CPointer<Long> = alloca_zero(size * 8).toCPointer<Long>().also { for (n in 0 until values.size) sd(it.ptr + n * 8, values[n]) }

    operator fun CPointer<UByte>.get(offset: Int): UByte = lb(this.ptr + offset * 1).toUByte()
    operator fun CPointer<UByte>.set(offset: Int, value: UByte) = sb(this.ptr + offset * 1, (value).toByte())
    var CPointer<UByte>.value: UByte get() = this[0]; set(value): Unit = run { this[0] = value }
    @JvmName("plusUByte") operator fun CPointer<UByte>.plus(offset: Int) = addPtr<UByte>(offset, 1)
    @JvmName("minusUByte") operator fun CPointer<UByte>.minus(offset: Int) = addPtr<UByte>(-offset, 1)
    @JvmName("minusUBytePtr") operator fun CPointer<UByte>.minus(other: CPointer<UByte>) = (this.ptr - other.ptr) / 1
    fun fixedArrayOfUByte(size: Int, vararg values: UByte): CPointer<UByte> = alloca_zero(size * 1).toCPointer<UByte>().also { for (n in 0 until values.size) sb(it.ptr + n * 1, (values[n]).toByte()) }

    operator fun CPointer<UShort>.get(offset: Int): UShort = lh(this.ptr + offset * 2).toUShort()
    operator fun CPointer<UShort>.set(offset: Int, value: UShort) = sh(this.ptr + offset * 2, (value).toShort())
    var CPointer<UShort>.value: UShort get() = this[0]; set(value): Unit = run { this[0] = value }
    @JvmName("plusUShort") operator fun CPointer<UShort>.plus(offset: Int) = addPtr<UShort>(offset, 2)
    @JvmName("minusUShort") operator fun CPointer<UShort>.minus(offset: Int) = addPtr<UShort>(-offset, 2)
    @JvmName("minusUShortPtr") operator fun CPointer<UShort>.minus(other: CPointer<UShort>) = (this.ptr - other.ptr) / 2
    fun fixedArrayOfUShort(size: Int, vararg values: UShort): CPointer<UShort> = alloca_zero(size * 2).toCPointer<UShort>().also { for (n in 0 until values.size) sh(it.ptr + n * 2, (values[n]).toShort()) }

    operator fun CPointer<UInt>.get(offset: Int): UInt = lw(this.ptr + offset * 4).toUInt()
    operator fun CPointer<UInt>.set(offset: Int, value: UInt) = sw(this.ptr + offset * 4, (value).toInt())
    var CPointer<UInt>.value: UInt get() = this[0]; set(value): Unit = run { this[0] = value }
    @JvmName("plusUInt") operator fun CPointer<UInt>.plus(offset: Int) = addPtr<UInt>(offset, 4)
    @JvmName("minusUInt") operator fun CPointer<UInt>.minus(offset: Int) = addPtr<UInt>(-offset, 4)
    @JvmName("minusUIntPtr") operator fun CPointer<UInt>.minus(other: CPointer<UInt>) = (this.ptr - other.ptr) / 4
    fun fixedArrayOfUInt(size: Int, vararg values: UInt): CPointer<UInt> = alloca_zero(size * 4).toCPointer<UInt>().also { for (n in 0 until values.size) sw(it.ptr + n * 4, (values[n]).toInt()) }

    operator fun CPointer<ULong>.get(offset: Int): ULong = ld(this.ptr + offset * 8).toULong()
    operator fun CPointer<ULong>.set(offset: Int, value: ULong) = sd(this.ptr + offset * 8, (value).toLong())
    var CPointer<ULong>.value: ULong get() = this[0]; set(value): Unit = run { this[0] = value }
    @JvmName("plusULong") operator fun CPointer<ULong>.plus(offset: Int) = addPtr<ULong>(offset, 8)
    @JvmName("minusULong") operator fun CPointer<ULong>.minus(offset: Int) = addPtr<ULong>(-offset, 8)
    @JvmName("minusULongPtr") operator fun CPointer<ULong>.minus(other: CPointer<ULong>) = (this.ptr - other.ptr) / 8
    fun fixedArrayOfULong(size: Int, vararg values: ULong): CPointer<ULong> = alloca_zero(size * 8).toCPointer<ULong>().also { for (n in 0 until values.size) sd(it.ptr + n * 8, (values[n]).toLong()) }

    @JvmName("getterFloat") operator fun CPointer<Float>.get(offset: Int): Float = Float.fromBits(lw(this.ptr + offset * 4))
    @JvmName("setterFloat") operator fun CPointer<Float>.set(offset: Int, value: Float) = sw(this.ptr + offset * 4, (value).toBits())
    @set:JvmName("setter_Float_value") @get:JvmName("getter_Float_value") var CPointer<Float>.value: Float get() = this[0]; set(value): Unit = run { this[0] = value }
    @JvmName("plusFloat") operator fun CPointer<Float>.plus(offset: Int) = addPtr<Float>(offset, 4)
    @JvmName("minusFloat") operator fun CPointer<Float>.minus(offset: Int) = addPtr<Float>(-offset, 4)
    @JvmName("minusFloatPtr") operator fun CPointer<Float>.minus(other: CPointer<Float>) = (this.ptr - other.ptr) / 4
    fun fixedArrayOfFloat(size: Int, vararg values: Float): CPointer<Float> = alloca_zero(size * 4).toCPointer<Float>().also { for (n in 0 until values.size) sw(it.ptr + n * 4, (values[n]).toBits()) }

    @JvmName("getterDouble") operator fun CPointer<Double>.get(offset: Int): Double = Double.fromBits(ld(this.ptr + offset * 4))
    @JvmName("setterDouble") operator fun CPointer<Double>.set(offset: Int, value: Double) = sd(this.ptr + offset * 4, (value).toBits())
    @set:JvmName("setter_Double_value") @get:JvmName("getter_Double_value") var CPointer<Double>.value: Double get() = this[0]; set(value): Unit = run { this[0] = value }
    @JvmName("plusDouble") operator fun CPointer<Double>.plus(offset: Int) = addPtr<Double>(offset, 4)
    @JvmName("minusDouble") operator fun CPointer<Double>.minus(offset: Int) = addPtr<Double>(-offset, 4)
    @JvmName("minusDoublePtr") operator fun CPointer<Double>.minus(other: CPointer<Double>) = (this.ptr - other.ptr) / 4
    fun fixedArrayOfDouble(size: Int, vararg values: Double): CPointer<Double> = alloca_zero(size * 4).toCPointer<Double>().also { for (n in 0 until values.size) sd(it.ptr + n * 4, (values[n]).toBits()) }


    val FUNCTION_ADDRS = LinkedHashMap<kotlin.reflect.KFunction<*>, Int>()

    operator fun <TR> CFunction0<TR>.invoke(): TR = (FUNCTIONS[this.ptr] as (() -> TR)).invoke()
    val <TR> kotlin.reflect.KFunction0<TR>.cfunc get() = CFunction0<TR>(FUNCTION_ADDRS.getOrPut(this) { FUNCTIONS.add(this); FUNCTIONS.size - 1 })
    operator fun <T0, TR> CFunction1<T0, TR>.invoke(v0: T0): TR = (FUNCTIONS[this.ptr] as ((T0) -> TR)).invoke(v0)
    val <T0, TR> kotlin.reflect.KFunction1<T0, TR>.cfunc get() = CFunction1<T0, TR>(FUNCTION_ADDRS.getOrPut(this) { FUNCTIONS.add(this); FUNCTIONS.size - 1 })
    operator fun <T0, T1, TR> CFunction2<T0, T1, TR>.invoke(v0: T0, v1: T1): TR = (FUNCTIONS[this.ptr] as ((T0, T1) -> TR)).invoke(v0, v1)
    val <T0, T1, TR> kotlin.reflect.KFunction2<T0, T1, TR>.cfunc get() = CFunction2<T0, T1, TR>(FUNCTION_ADDRS.getOrPut(this) { FUNCTIONS.add(this); FUNCTIONS.size - 1 })
    operator fun <T0, T1, T2, TR> CFunction3<T0, T1, T2, TR>.invoke(v0: T0, v1: T1, v2: T2): TR = (FUNCTIONS[this.ptr] as ((T0, T1, T2) -> TR)).invoke(v0, v1, v2)
    val <T0, T1, T2, TR> kotlin.reflect.KFunction3<T0, T1, T2, TR>.cfunc get() = CFunction3<T0, T1, T2, TR>(FUNCTION_ADDRS.getOrPut(this) { FUNCTIONS.add(this); FUNCTIONS.size - 1 })
    operator fun <T0, T1, T2, T3, TR> CFunction4<T0, T1, T2, T3, TR>.invoke(v0: T0, v1: T1, v2: T2, v3: T3): TR = (FUNCTIONS[this.ptr] as ((T0, T1, T2, T3) -> TR)).invoke(v0, v1, v2, v3)
    val <T0, T1, T2, T3, TR> kotlin.reflect.KFunction4<T0, T1, T2, T3, TR>.cfunc get() = CFunction4<T0, T1, T2, T3, TR>(FUNCTION_ADDRS.getOrPut(this) { FUNCTIONS.add(this); FUNCTIONS.size - 1 })
    operator fun <T0, T1, T2, T3, T4, TR> CFunction5<T0, T1, T2, T3, T4, TR>.invoke(v0: T0, v1: T1, v2: T2, v3: T3, v4: T4): TR = (FUNCTIONS[this.ptr] as ((T0, T1, T2, T3, T4) -> TR)).invoke(v0, v1, v2, v3, v4)
    val <T0, T1, T2, T3, T4, TR> kotlin.reflect.KFunction5<T0, T1, T2, T3, T4, TR>.cfunc get() = CFunction5<T0, T1, T2, T3, T4, TR>(FUNCTION_ADDRS.getOrPut(this) { FUNCTIONS.add(this); FUNCTIONS.size - 1 })
    operator fun <T0, T1, T2, T3, T4, T5, TR> CFunction6<T0, T1, T2, T3, T4, T5, TR>.invoke(v0: T0, v1: T1, v2: T2, v3: T3, v4: T4, v5: T5): TR = (FUNCTIONS[this.ptr] as ((T0, T1, T2, T3, T4, T5) -> TR)).invoke(v0, v1, v2, v3, v4, v5)
    val <T0, T1, T2, T3, T4, T5, TR> kotlin.reflect.KFunction6<T0, T1, T2, T3, T4, T5, TR>.cfunc get() = CFunction6<T0, T1, T2, T3, T4, T5, TR>(FUNCTION_ADDRS.getOrPut(this) { FUNCTIONS.add(this); FUNCTIONS.size - 1 })
    operator fun <T0, T1, T2, T3, T4, T5, T6, TR> CFunction7<T0, T1, T2, T3, T4, T5, T6, TR>.invoke(v0: T0, v1: T1, v2: T2, v3: T3, v4: T4, v5: T5, v6: T6): TR = (FUNCTIONS[this.ptr] as ((T0, T1, T2, T3, T4, T5, T6) -> TR)).invoke(v0, v1, v2, v3, v4, v5, v6)
    val <T0, T1, T2, T3, T4, T5, T6, TR> kotlin.reflect.KFunction7<T0, T1, T2, T3, T4, T5, T6, TR>.cfunc get() = CFunction7<T0, T1, T2, T3, T4, T5, T6, TR>(FUNCTION_ADDRS.getOrPut(this) { FUNCTIONS.add(this); FUNCTIONS.size - 1 })
}
