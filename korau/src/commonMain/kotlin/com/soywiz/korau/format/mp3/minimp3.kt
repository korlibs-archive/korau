package com.soywiz.korau.format.mp3



//ENTRY Program
//Program.main(arrayOf())
@Suppress("MemberVisibilityCanBePrivate", "FunctionName", "CanBeVal", "DoubleNegation", "LocalVariableName", "NAME_SHADOWING", "VARIABLE_WITH_REDUNDANT_INITIALIZER", "RemoveRedundantCallsOfConversionMethods", "EXPERIMENTAL_IS_NOT_ENABLED", "RedundantExplicitType", "RemoveExplicitTypeArguments", "RedundantExplicitType", "unused", "UNCHECKED_CAST", "UNUSED_VARIABLE", "UNUSED_PARAMETER", "NOTHING_TO_INLINE", "PropertyName", "ClassName", "USELESS_CAST", "PrivatePropertyName", "CanBeParameter", "UnusedMainParameter")
@UseExperimental(ExperimentalUnsignedTypes::class)
public class MiniMp3(HEAP_SIZE: Int = 0) : Runtime(HEAP_SIZE) {
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
        const val BITS_DEQUANTIZER_OUT = -1
        const val MAX_SCF = 41
        const val HAVE_SIMD = 0
    }

    private var __STATIC_hdr_bitrate_kbps_halfrate: Array2Array3Array15UByte = { Array2Array3Array15UByteAlloc { this[0] = Array3Array15UByteAlloc { this[0] = Array15UByteAlloc { this[0] = ((0).toUByte()); this[1] = ((4).toUByte()); this[2] = ((8).toUByte()); this[3] = ((12).toUByte()); this[4] = ((16).toUByte()); this[5] = ((20).toUByte()); this[6] = ((24).toUByte()); this[7] = ((28).toUByte()); this[8] = ((32).toUByte()); this[9] = ((40).toUByte()); this[10] = ((48).toUByte()); this[11] = ((56).toUByte()); this[12] = ((64).toUByte()); this[13] = ((72).toUByte()); this[14] = ((80).toUByte()) }; this[1] = Array15UByteAlloc { this[0] = ((0).toUByte()); this[1] = ((4).toUByte()); this[2] = ((8).toUByte()); this[3] = ((12).toUByte()); this[4] = ((16).toUByte()); this[5] = ((20).toUByte()); this[6] = ((24).toUByte()); this[7] = ((28).toUByte()); this[8] = ((32).toUByte()); this[9] = ((40).toUByte()); this[10] = ((48).toUByte()); this[11] = ((56).toUByte()); this[12] = ((64).toUByte()); this[13] = ((72).toUByte()); this[14] = ((80).toUByte()) }; this[2] = Array15UByteAlloc { this[0] = ((0).toUByte()); this[1] = ((16).toUByte()); this[2] = ((24).toUByte()); this[3] = ((28).toUByte()); this[4] = ((32).toUByte()); this[5] = ((40).toUByte()); this[6] = ((48).toUByte()); this[7] = ((56).toUByte()); this[8] = ((64).toUByte()); this[9] = ((72).toUByte()); this[10] = ((80).toUByte()); this[11] = ((88).toUByte()); this[12] = ((96).toUByte()); this[13] = ((112).toUByte()); this[14] = ((128).toUByte()) } }; this[1] = Array3Array15UByteAlloc { this[0] = Array15UByteAlloc { this[0] = ((0).toUByte()); this[1] = ((16).toUByte()); this[2] = ((20).toUByte()); this[3] = ((24).toUByte()); this[4] = ((28).toUByte()); this[5] = ((32).toUByte()); this[6] = ((40).toUByte()); this[7] = ((48).toUByte()); this[8] = ((56).toUByte()); this[9] = ((64).toUByte()); this[10] = ((80).toUByte()); this[11] = ((96).toUByte()); this[12] = ((112).toUByte()); this[13] = ((128).toUByte()); this[14] = ((160).toUByte()) }; this[1] = Array15UByteAlloc { this[0] = ((0).toUByte()); this[1] = ((16).toUByte()); this[2] = ((24).toUByte()); this[3] = ((28).toUByte()); this[4] = ((32).toUByte()); this[5] = ((40).toUByte()); this[6] = ((48).toUByte()); this[7] = ((56).toUByte()); this[8] = ((64).toUByte()); this[9] = ((80).toUByte()); this[10] = ((96).toUByte()); this[11] = ((112).toUByte()); this[12] = ((128).toUByte()); this[13] = ((160).toUByte()); this[14] = ((192).toUByte()) }; this[2] = Array15UByteAlloc { this[0] = ((0).toUByte()); this[1] = ((16).toUByte()); this[2] = ((32).toUByte()); this[3] = ((48).toUByte()); this[4] = ((64).toUByte()); this[5] = ((80).toUByte()); this[6] = ((96).toUByte()); this[7] = ((112).toUByte()); this[8] = ((128).toUByte()); this[9] = ((144).toUByte()); this[10] = ((160).toUByte()); this[11] = ((176).toUByte()); this[12] = ((192).toUByte()); this[13] = ((208).toUByte()); this[14] = ((224).toUByte()) } } } }()
    private var __STATIC_hdr_sample_rate_hz_g_hz: Array3UInt = { Array3UIntAlloc { this[0] = 44100u; this[1] = 48000u; this[2] = 32000u } }()
    private var __STATIC_L12_subband_alloc_table_g_alloc_L1: CPointer<L12_subband_alloc_t> = { fixedArrayOfL12_subband_alloc_t(1) { this[0] = L12_subband_alloc_tAlloc(tab_offset = ((76).toUByte()), code_tab_width = ((4).toUByte()), band_count = ((32).toUByte())) } }()
    private var __STATIC_L12_subband_alloc_table_g_alloc_L2M2: CPointer<L12_subband_alloc_t> = { fixedArrayOfL12_subband_alloc_t(3) { this[0] = L12_subband_alloc_tAlloc(tab_offset = ((60).toUByte()), code_tab_width = ((4).toUByte()), band_count = ((4).toUByte())); this[1] = L12_subband_alloc_tAlloc(tab_offset = ((44).toUByte()), code_tab_width = ((3).toUByte()), band_count = ((7).toUByte())); this[2] = L12_subband_alloc_tAlloc(tab_offset = ((44).toUByte()), code_tab_width = ((2).toUByte()), band_count = ((19).toUByte())) } }()
    private var __STATIC_L12_subband_alloc_table_g_alloc_L2M1: CPointer<L12_subband_alloc_t> = { fixedArrayOfL12_subband_alloc_t(4) { this[0] = L12_subband_alloc_tAlloc(tab_offset = ((0).toUByte()), code_tab_width = ((4).toUByte()), band_count = ((3).toUByte())); this[1] = L12_subband_alloc_tAlloc(tab_offset = ((16).toUByte()), code_tab_width = ((4).toUByte()), band_count = ((8).toUByte())); this[2] = L12_subband_alloc_tAlloc(tab_offset = ((32).toUByte()), code_tab_width = ((3).toUByte()), band_count = ((12).toUByte())); this[3] = L12_subband_alloc_tAlloc(tab_offset = ((40).toUByte()), code_tab_width = ((2).toUByte()), band_count = ((7).toUByte())) } }()
    private var __STATIC_L12_subband_alloc_table_g_alloc_L2M1_lowrate: CPointer<L12_subband_alloc_t> = { fixedArrayOfL12_subband_alloc_t(2) { this[0] = L12_subband_alloc_tAlloc(tab_offset = ((44).toUByte()), code_tab_width = ((4).toUByte()), band_count = ((2).toUByte())); this[1] = L12_subband_alloc_tAlloc(tab_offset = ((44).toUByte()), code_tab_width = ((3).toUByte()), band_count = ((10).toUByte())) } }()
    private var __STATIC_L12_read_scalefactors_g_deq_L12: Array54Float = { Array54FloatAlloc { this[0] = (9.53674316E-7f / 3f); this[1] = (7.56931807E-7f / 3f); this[2] = (6.00777173E-7f / 3f); this[3] = (9.53674316E-7f / 7f); this[4] = (7.56931807E-7f / 7f); this[5] = (6.00777173E-7f / 7f); this[6] = (9.53674316E-7f / 15f); this[7] = (7.56931807E-7f / 15f); this[8] = (6.00777173E-7f / 15f); this[9] = (9.53674316E-7f / 31f); this[10] = (7.56931807E-7f / 31f); this[11] = (6.00777173E-7f / 31f); this[12] = (9.53674316E-7f / 63f); this[13] = (7.56931807E-7f / 63f); this[14] = (6.00777173E-7f / 63f); this[15] = (9.53674316E-7f / 127f); this[16] = (7.56931807E-7f / 127f); this[17] = (6.00777173E-7f / 127f); this[18] = (9.53674316E-7f / 255f); this[19] = (7.56931807E-7f / 255f); this[20] = (6.00777173E-7f / 255f); this[21] = (9.53674316E-7f / 511f); this[22] = (7.56931807E-7f / 511f); this[23] = (6.00777173E-7f / 511f); this[24] = (9.53674316E-7f / 1023f); this[25] = (7.56931807E-7f / 1023f); this[26] = (6.00777173E-7f / 1023f); this[27] = (9.53674316E-7f / 2047f); this[28] = (7.56931807E-7f / 2047f); this[29] = (6.00777173E-7f / 2047f); this[30] = (9.53674316E-7f / 4095f); this[31] = (7.56931807E-7f / 4095f); this[32] = (6.00777173E-7f / 4095f); this[33] = (9.53674316E-7f / 8191f); this[34] = (7.56931807E-7f / 8191f); this[35] = (6.00777173E-7f / 8191f); this[36] = (9.53674316E-7f / 16383f); this[37] = (7.56931807E-7f / 16383f); this[38] = (6.00777173E-7f / 16383f); this[39] = (9.53674316E-7f / 32767f); this[40] = (7.56931807E-7f / 32767f); this[41] = (6.00777173E-7f / 32767f); this[42] = (9.53674316E-7f / 65535f); this[43] = (7.56931807E-7f / 65535f); this[44] = (6.00777173E-7f / 65535f); this[45] = (9.53674316E-7f / 3f); this[46] = (7.56931807E-7f / 3f); this[47] = (6.00777173E-7f / 3f); this[48] = (9.53674316E-7f / 5f); this[49] = (7.56931807E-7f / 5f); this[50] = (6.00777173E-7f / 5f); this[51] = (9.53674316E-7f / 9f); this[52] = (7.56931807E-7f / 9f); this[53] = (6.00777173E-7f / 9f) } }()
    private var __STATIC_L12_read_scale_info_g_bitalloc_code_tab: CPointer<UByte> = { fixedArrayOfUByte(92) { this[0] = ((0).toUByte()); this[1] = ((17).toUByte()); this[2] = ((3).toUByte()); this[3] = ((4).toUByte()); this[4] = ((5).toUByte()); this[5] = ((6).toUByte()); this[6] = ((7).toUByte()); this[7] = ((8).toUByte()); this[8] = ((9).toUByte()); this[9] = ((10).toUByte()); this[10] = ((11).toUByte()); this[11] = ((12).toUByte()); this[12] = ((13).toUByte()); this[13] = ((14).toUByte()); this[14] = ((15).toUByte()); this[15] = ((16).toUByte()); this[16] = ((0).toUByte()); this[17] = ((17).toUByte()); this[18] = ((18).toUByte()); this[19] = ((3).toUByte()); this[20] = ((19).toUByte()); this[21] = ((4).toUByte()); this[22] = ((5).toUByte()); this[23] = ((6).toUByte()); this[24] = ((7).toUByte()); this[25] = ((8).toUByte()); this[26] = ((9).toUByte()); this[27] = ((10).toUByte()); this[28] = ((11).toUByte()); this[29] = ((12).toUByte()); this[30] = ((13).toUByte()); this[31] = ((16).toUByte()); this[32] = ((0).toUByte()); this[33] = ((17).toUByte()); this[34] = ((18).toUByte()); this[35] = ((3).toUByte()); this[36] = ((19).toUByte()); this[37] = ((4).toUByte()); this[38] = ((5).toUByte()); this[39] = ((16).toUByte()); this[40] = ((0).toUByte()); this[41] = ((17).toUByte()); this[42] = ((18).toUByte()); this[43] = ((16).toUByte()); this[44] = ((0).toUByte()); this[45] = ((17).toUByte()); this[46] = ((18).toUByte()); this[47] = ((19).toUByte()); this[48] = ((4).toUByte()); this[49] = ((5).toUByte()); this[50] = ((6).toUByte()); this[51] = ((7).toUByte()); this[52] = ((8).toUByte()); this[53] = ((9).toUByte()); this[54] = ((10).toUByte()); this[55] = ((11).toUByte()); this[56] = ((12).toUByte()); this[57] = ((13).toUByte()); this[58] = ((14).toUByte()); this[59] = ((15).toUByte()); this[60] = ((0).toUByte()); this[61] = ((17).toUByte()); this[62] = ((18).toUByte()); this[63] = ((3).toUByte()); this[64] = ((19).toUByte()); this[65] = ((4).toUByte()); this[66] = ((5).toUByte()); this[67] = ((6).toUByte()); this[68] = ((7).toUByte()); this[69] = ((8).toUByte()); this[70] = ((9).toUByte()); this[71] = ((10).toUByte()); this[72] = ((11).toUByte()); this[73] = ((12).toUByte()); this[74] = ((13).toUByte()); this[75] = ((14).toUByte()); this[76] = ((0).toUByte()); this[77] = ((2).toUByte()); this[78] = ((3).toUByte()); this[79] = ((4).toUByte()); this[80] = ((5).toUByte()); this[81] = ((6).toUByte()); this[82] = ((7).toUByte()); this[83] = ((8).toUByte()); this[84] = ((9).toUByte()); this[85] = ((10).toUByte()); this[86] = ((11).toUByte()); this[87] = ((12).toUByte()); this[88] = ((13).toUByte()); this[89] = ((14).toUByte()); this[90] = ((15).toUByte()); this[91] = ((16).toUByte()) } }()
    private var __STATIC_L3_read_side_info_g_scf_long: Array8Array23UByte = { Array8Array23UByteAlloc { this[0] = Array23UByteAlloc { this[0] = ((6).toUByte()); this[1] = ((6).toUByte()); this[2] = ((6).toUByte()); this[3] = ((6).toUByte()); this[4] = ((6).toUByte()); this[5] = ((6).toUByte()); this[6] = ((8).toUByte()); this[7] = ((10).toUByte()); this[8] = ((12).toUByte()); this[9] = ((14).toUByte()); this[10] = ((16).toUByte()); this[11] = ((20).toUByte()); this[12] = ((24).toUByte()); this[13] = ((28).toUByte()); this[14] = ((32).toUByte()); this[15] = ((38).toUByte()); this[16] = ((46).toUByte()); this[17] = ((52).toUByte()); this[18] = ((60).toUByte()); this[19] = ((68).toUByte()); this[20] = ((58).toUByte()); this[21] = ((54).toUByte()); this[22] = ((0).toUByte()) }; this[1] = Array23UByteAlloc { this[0] = ((12).toUByte()); this[1] = ((12).toUByte()); this[2] = ((12).toUByte()); this[3] = ((12).toUByte()); this[4] = ((12).toUByte()); this[5] = ((12).toUByte()); this[6] = ((16).toUByte()); this[7] = ((20).toUByte()); this[8] = ((24).toUByte()); this[9] = ((28).toUByte()); this[10] = ((32).toUByte()); this[11] = ((40).toUByte()); this[12] = ((48).toUByte()); this[13] = ((56).toUByte()); this[14] = ((64).toUByte()); this[15] = ((76).toUByte()); this[16] = ((90).toUByte()); this[17] = ((2).toUByte()); this[18] = ((2).toUByte()); this[19] = ((2).toUByte()); this[20] = ((2).toUByte()); this[21] = ((2).toUByte()); this[22] = ((0).toUByte()) }; this[2] = Array23UByteAlloc { this[0] = ((6).toUByte()); this[1] = ((6).toUByte()); this[2] = ((6).toUByte()); this[3] = ((6).toUByte()); this[4] = ((6).toUByte()); this[5] = ((6).toUByte()); this[6] = ((8).toUByte()); this[7] = ((10).toUByte()); this[8] = ((12).toUByte()); this[9] = ((14).toUByte()); this[10] = ((16).toUByte()); this[11] = ((20).toUByte()); this[12] = ((24).toUByte()); this[13] = ((28).toUByte()); this[14] = ((32).toUByte()); this[15] = ((38).toUByte()); this[16] = ((46).toUByte()); this[17] = ((52).toUByte()); this[18] = ((60).toUByte()); this[19] = ((68).toUByte()); this[20] = ((58).toUByte()); this[21] = ((54).toUByte()); this[22] = ((0).toUByte()) }; this[3] = Array23UByteAlloc { this[0] = ((6).toUByte()); this[1] = ((6).toUByte()); this[2] = ((6).toUByte()); this[3] = ((6).toUByte()); this[4] = ((6).toUByte()); this[5] = ((6).toUByte()); this[6] = ((8).toUByte()); this[7] = ((10).toUByte()); this[8] = ((12).toUByte()); this[9] = ((14).toUByte()); this[10] = ((16).toUByte()); this[11] = ((18).toUByte()); this[12] = ((22).toUByte()); this[13] = ((26).toUByte()); this[14] = ((32).toUByte()); this[15] = ((38).toUByte()); this[16] = ((46).toUByte()); this[17] = ((54).toUByte()); this[18] = ((62).toUByte()); this[19] = ((70).toUByte()); this[20] = ((76).toUByte()); this[21] = ((36).toUByte()); this[22] = ((0).toUByte()) }; this[4] = Array23UByteAlloc { this[0] = ((6).toUByte()); this[1] = ((6).toUByte()); this[2] = ((6).toUByte()); this[3] = ((6).toUByte()); this[4] = ((6).toUByte()); this[5] = ((6).toUByte()); this[6] = ((8).toUByte()); this[7] = ((10).toUByte()); this[8] = ((12).toUByte()); this[9] = ((14).toUByte()); this[10] = ((16).toUByte()); this[11] = ((20).toUByte()); this[12] = ((24).toUByte()); this[13] = ((28).toUByte()); this[14] = ((32).toUByte()); this[15] = ((38).toUByte()); this[16] = ((46).toUByte()); this[17] = ((52).toUByte()); this[18] = ((60).toUByte()); this[19] = ((68).toUByte()); this[20] = ((58).toUByte()); this[21] = ((54).toUByte()); this[22] = ((0).toUByte()) }; this[5] = Array23UByteAlloc { this[0] = ((4).toUByte()); this[1] = ((4).toUByte()); this[2] = ((4).toUByte()); this[3] = ((4).toUByte()); this[4] = ((4).toUByte()); this[5] = ((4).toUByte()); this[6] = ((6).toUByte()); this[7] = ((6).toUByte()); this[8] = ((8).toUByte()); this[9] = ((8).toUByte()); this[10] = ((10).toUByte()); this[11] = ((12).toUByte()); this[12] = ((16).toUByte()); this[13] = ((20).toUByte()); this[14] = ((24).toUByte()); this[15] = ((28).toUByte()); this[16] = ((34).toUByte()); this[17] = ((42).toUByte()); this[18] = ((50).toUByte()); this[19] = ((54).toUByte()); this[20] = ((76).toUByte()); this[21] = ((158).toUByte()); this[22] = ((0).toUByte()) }; this[6] = Array23UByteAlloc { this[0] = ((4).toUByte()); this[1] = ((4).toUByte()); this[2] = ((4).toUByte()); this[3] = ((4).toUByte()); this[4] = ((4).toUByte()); this[5] = ((4).toUByte()); this[6] = ((6).toUByte()); this[7] = ((6).toUByte()); this[8] = ((6).toUByte()); this[9] = ((8).toUByte()); this[10] = ((10).toUByte()); this[11] = ((12).toUByte()); this[12] = ((16).toUByte()); this[13] = ((18).toUByte()); this[14] = ((22).toUByte()); this[15] = ((28).toUByte()); this[16] = ((34).toUByte()); this[17] = ((40).toUByte()); this[18] = ((46).toUByte()); this[19] = ((54).toUByte()); this[20] = ((54).toUByte()); this[21] = ((192).toUByte()); this[22] = ((0).toUByte()) }; this[7] = Array23UByteAlloc { this[0] = ((4).toUByte()); this[1] = ((4).toUByte()); this[2] = ((4).toUByte()); this[3] = ((4).toUByte()); this[4] = ((4).toUByte()); this[5] = ((4).toUByte()); this[6] = ((6).toUByte()); this[7] = ((6).toUByte()); this[8] = ((8).toUByte()); this[9] = ((10).toUByte()); this[10] = ((12).toUByte()); this[11] = ((16).toUByte()); this[12] = ((20).toUByte()); this[13] = ((24).toUByte()); this[14] = ((30).toUByte()); this[15] = ((38).toUByte()); this[16] = ((46).toUByte()); this[17] = ((56).toUByte()); this[18] = ((68).toUByte()); this[19] = ((84).toUByte()); this[20] = ((102).toUByte()); this[21] = ((26).toUByte()); this[22] = ((0).toUByte()) } } }()
    private var __STATIC_L3_read_side_info_g_scf_short: Array8Array40UByte = { Array8Array40UByteAlloc { this[0] = Array40UByteAlloc { this[0] = ((4).toUByte()); this[1] = ((4).toUByte()); this[2] = ((4).toUByte()); this[3] = ((4).toUByte()); this[4] = ((4).toUByte()); this[5] = ((4).toUByte()); this[6] = ((4).toUByte()); this[7] = ((4).toUByte()); this[8] = ((4).toUByte()); this[9] = ((6).toUByte()); this[10] = ((6).toUByte()); this[11] = ((6).toUByte()); this[12] = ((8).toUByte()); this[13] = ((8).toUByte()); this[14] = ((8).toUByte()); this[15] = ((10).toUByte()); this[16] = ((10).toUByte()); this[17] = ((10).toUByte()); this[18] = ((12).toUByte()); this[19] = ((12).toUByte()); this[20] = ((12).toUByte()); this[21] = ((14).toUByte()); this[22] = ((14).toUByte()); this[23] = ((14).toUByte()); this[24] = ((18).toUByte()); this[25] = ((18).toUByte()); this[26] = ((18).toUByte()); this[27] = ((24).toUByte()); this[28] = ((24).toUByte()); this[29] = ((24).toUByte()); this[30] = ((30).toUByte()); this[31] = ((30).toUByte()); this[32] = ((30).toUByte()); this[33] = ((40).toUByte()); this[34] = ((40).toUByte()); this[35] = ((40).toUByte()); this[36] = ((18).toUByte()); this[37] = ((18).toUByte()); this[38] = ((18).toUByte()); this[39] = ((0).toUByte()) }; this[1] = Array40UByteAlloc { this[0] = ((8).toUByte()); this[1] = ((8).toUByte()); this[2] = ((8).toUByte()); this[3] = ((8).toUByte()); this[4] = ((8).toUByte()); this[5] = ((8).toUByte()); this[6] = ((8).toUByte()); this[7] = ((8).toUByte()); this[8] = ((8).toUByte()); this[9] = ((12).toUByte()); this[10] = ((12).toUByte()); this[11] = ((12).toUByte()); this[12] = ((16).toUByte()); this[13] = ((16).toUByte()); this[14] = ((16).toUByte()); this[15] = ((20).toUByte()); this[16] = ((20).toUByte()); this[17] = ((20).toUByte()); this[18] = ((24).toUByte()); this[19] = ((24).toUByte()); this[20] = ((24).toUByte()); this[21] = ((28).toUByte()); this[22] = ((28).toUByte()); this[23] = ((28).toUByte()); this[24] = ((36).toUByte()); this[25] = ((36).toUByte()); this[26] = ((36).toUByte()); this[27] = ((2).toUByte()); this[28] = ((2).toUByte()); this[29] = ((2).toUByte()); this[30] = ((2).toUByte()); this[31] = ((2).toUByte()); this[32] = ((2).toUByte()); this[33] = ((2).toUByte()); this[34] = ((2).toUByte()); this[35] = ((2).toUByte()); this[36] = ((26).toUByte()); this[37] = ((26).toUByte()); this[38] = ((26).toUByte()); this[39] = ((0).toUByte()) }; this[2] = Array40UByteAlloc { this[0] = ((4).toUByte()); this[1] = ((4).toUByte()); this[2] = ((4).toUByte()); this[3] = ((4).toUByte()); this[4] = ((4).toUByte()); this[5] = ((4).toUByte()); this[6] = ((4).toUByte()); this[7] = ((4).toUByte()); this[8] = ((4).toUByte()); this[9] = ((6).toUByte()); this[10] = ((6).toUByte()); this[11] = ((6).toUByte()); this[12] = ((6).toUByte()); this[13] = ((6).toUByte()); this[14] = ((6).toUByte()); this[15] = ((8).toUByte()); this[16] = ((8).toUByte()); this[17] = ((8).toUByte()); this[18] = ((10).toUByte()); this[19] = ((10).toUByte()); this[20] = ((10).toUByte()); this[21] = ((14).toUByte()); this[22] = ((14).toUByte()); this[23] = ((14).toUByte()); this[24] = ((18).toUByte()); this[25] = ((18).toUByte()); this[26] = ((18).toUByte()); this[27] = ((26).toUByte()); this[28] = ((26).toUByte()); this[29] = ((26).toUByte()); this[30] = ((32).toUByte()); this[31] = ((32).toUByte()); this[32] = ((32).toUByte()); this[33] = ((42).toUByte()); this[34] = ((42).toUByte()); this[35] = ((42).toUByte()); this[36] = ((18).toUByte()); this[37] = ((18).toUByte()); this[38] = ((18).toUByte()); this[39] = ((0).toUByte()) }; this[3] = Array40UByteAlloc { this[0] = ((4).toUByte()); this[1] = ((4).toUByte()); this[2] = ((4).toUByte()); this[3] = ((4).toUByte()); this[4] = ((4).toUByte()); this[5] = ((4).toUByte()); this[6] = ((4).toUByte()); this[7] = ((4).toUByte()); this[8] = ((4).toUByte()); this[9] = ((6).toUByte()); this[10] = ((6).toUByte()); this[11] = ((6).toUByte()); this[12] = ((8).toUByte()); this[13] = ((8).toUByte()); this[14] = ((8).toUByte()); this[15] = ((10).toUByte()); this[16] = ((10).toUByte()); this[17] = ((10).toUByte()); this[18] = ((12).toUByte()); this[19] = ((12).toUByte()); this[20] = ((12).toUByte()); this[21] = ((14).toUByte()); this[22] = ((14).toUByte()); this[23] = ((14).toUByte()); this[24] = ((18).toUByte()); this[25] = ((18).toUByte()); this[26] = ((18).toUByte()); this[27] = ((24).toUByte()); this[28] = ((24).toUByte()); this[29] = ((24).toUByte()); this[30] = ((32).toUByte()); this[31] = ((32).toUByte()); this[32] = ((32).toUByte()); this[33] = ((44).toUByte()); this[34] = ((44).toUByte()); this[35] = ((44).toUByte()); this[36] = ((12).toUByte()); this[37] = ((12).toUByte()); this[38] = ((12).toUByte()); this[39] = ((0).toUByte()) }; this[4] = Array40UByteAlloc { this[0] = ((4).toUByte()); this[1] = ((4).toUByte()); this[2] = ((4).toUByte()); this[3] = ((4).toUByte()); this[4] = ((4).toUByte()); this[5] = ((4).toUByte()); this[6] = ((4).toUByte()); this[7] = ((4).toUByte()); this[8] = ((4).toUByte()); this[9] = ((6).toUByte()); this[10] = ((6).toUByte()); this[11] = ((6).toUByte()); this[12] = ((8).toUByte()); this[13] = ((8).toUByte()); this[14] = ((8).toUByte()); this[15] = ((10).toUByte()); this[16] = ((10).toUByte()); this[17] = ((10).toUByte()); this[18] = ((12).toUByte()); this[19] = ((12).toUByte()); this[20] = ((12).toUByte()); this[21] = ((14).toUByte()); this[22] = ((14).toUByte()); this[23] = ((14).toUByte()); this[24] = ((18).toUByte()); this[25] = ((18).toUByte()); this[26] = ((18).toUByte()); this[27] = ((24).toUByte()); this[28] = ((24).toUByte()); this[29] = ((24).toUByte()); this[30] = ((30).toUByte()); this[31] = ((30).toUByte()); this[32] = ((30).toUByte()); this[33] = ((40).toUByte()); this[34] = ((40).toUByte()); this[35] = ((40).toUByte()); this[36] = ((18).toUByte()); this[37] = ((18).toUByte()); this[38] = ((18).toUByte()); this[39] = ((0).toUByte()) }; this[5] = Array40UByteAlloc { this[0] = ((4).toUByte()); this[1] = ((4).toUByte()); this[2] = ((4).toUByte()); this[3] = ((4).toUByte()); this[4] = ((4).toUByte()); this[5] = ((4).toUByte()); this[6] = ((4).toUByte()); this[7] = ((4).toUByte()); this[8] = ((4).toUByte()); this[9] = ((4).toUByte()); this[10] = ((4).toUByte()); this[11] = ((4).toUByte()); this[12] = ((6).toUByte()); this[13] = ((6).toUByte()); this[14] = ((6).toUByte()); this[15] = ((8).toUByte()); this[16] = ((8).toUByte()); this[17] = ((8).toUByte()); this[18] = ((10).toUByte()); this[19] = ((10).toUByte()); this[20] = ((10).toUByte()); this[21] = ((12).toUByte()); this[22] = ((12).toUByte()); this[23] = ((12).toUByte()); this[24] = ((14).toUByte()); this[25] = ((14).toUByte()); this[26] = ((14).toUByte()); this[27] = ((18).toUByte()); this[28] = ((18).toUByte()); this[29] = ((18).toUByte()); this[30] = ((22).toUByte()); this[31] = ((22).toUByte()); this[32] = ((22).toUByte()); this[33] = ((30).toUByte()); this[34] = ((30).toUByte()); this[35] = ((30).toUByte()); this[36] = ((56).toUByte()); this[37] = ((56).toUByte()); this[38] = ((56).toUByte()); this[39] = ((0).toUByte()) }; this[6] = Array40UByteAlloc { this[0] = ((4).toUByte()); this[1] = ((4).toUByte()); this[2] = ((4).toUByte()); this[3] = ((4).toUByte()); this[4] = ((4).toUByte()); this[5] = ((4).toUByte()); this[6] = ((4).toUByte()); this[7] = ((4).toUByte()); this[8] = ((4).toUByte()); this[9] = ((4).toUByte()); this[10] = ((4).toUByte()); this[11] = ((4).toUByte()); this[12] = ((6).toUByte()); this[13] = ((6).toUByte()); this[14] = ((6).toUByte()); this[15] = ((6).toUByte()); this[16] = ((6).toUByte()); this[17] = ((6).toUByte()); this[18] = ((10).toUByte()); this[19] = ((10).toUByte()); this[20] = ((10).toUByte()); this[21] = ((12).toUByte()); this[22] = ((12).toUByte()); this[23] = ((12).toUByte()); this[24] = ((14).toUByte()); this[25] = ((14).toUByte()); this[26] = ((14).toUByte()); this[27] = ((16).toUByte()); this[28] = ((16).toUByte()); this[29] = ((16).toUByte()); this[30] = ((20).toUByte()); this[31] = ((20).toUByte()); this[32] = ((20).toUByte()); this[33] = ((26).toUByte()); this[34] = ((26).toUByte()); this[35] = ((26).toUByte()); this[36] = ((66).toUByte()); this[37] = ((66).toUByte()); this[38] = ((66).toUByte()); this[39] = ((0).toUByte()) }; this[7] = Array40UByteAlloc { this[0] = ((4).toUByte()); this[1] = ((4).toUByte()); this[2] = ((4).toUByte()); this[3] = ((4).toUByte()); this[4] = ((4).toUByte()); this[5] = ((4).toUByte()); this[6] = ((4).toUByte()); this[7] = ((4).toUByte()); this[8] = ((4).toUByte()); this[9] = ((4).toUByte()); this[10] = ((4).toUByte()); this[11] = ((4).toUByte()); this[12] = ((6).toUByte()); this[13] = ((6).toUByte()); this[14] = ((6).toUByte()); this[15] = ((8).toUByte()); this[16] = ((8).toUByte()); this[17] = ((8).toUByte()); this[18] = ((12).toUByte()); this[19] = ((12).toUByte()); this[20] = ((12).toUByte()); this[21] = ((16).toUByte()); this[22] = ((16).toUByte()); this[23] = ((16).toUByte()); this[24] = ((20).toUByte()); this[25] = ((20).toUByte()); this[26] = ((20).toUByte()); this[27] = ((26).toUByte()); this[28] = ((26).toUByte()); this[29] = ((26).toUByte()); this[30] = ((34).toUByte()); this[31] = ((34).toUByte()); this[32] = ((34).toUByte()); this[33] = ((42).toUByte()); this[34] = ((42).toUByte()); this[35] = ((42).toUByte()); this[36] = ((12).toUByte()); this[37] = ((12).toUByte()); this[38] = ((12).toUByte()); this[39] = ((0).toUByte()) } } }()
    private var __STATIC_L3_read_side_info_g_scf_mixed: Array8Array40UByte = { Array8Array40UByteAlloc { this[0] = Array40UByteAlloc { this[0] = ((6).toUByte()); this[1] = ((6).toUByte()); this[2] = ((6).toUByte()); this[3] = ((6).toUByte()); this[4] = ((6).toUByte()); this[5] = ((6).toUByte()); this[6] = ((6).toUByte()); this[7] = ((6).toUByte()); this[8] = ((6).toUByte()); this[9] = ((8).toUByte()); this[10] = ((8).toUByte()); this[11] = ((8).toUByte()); this[12] = ((10).toUByte()); this[13] = ((10).toUByte()); this[14] = ((10).toUByte()); this[15] = ((12).toUByte()); this[16] = ((12).toUByte()); this[17] = ((12).toUByte()); this[18] = ((14).toUByte()); this[19] = ((14).toUByte()); this[20] = ((14).toUByte()); this[21] = ((18).toUByte()); this[22] = ((18).toUByte()); this[23] = ((18).toUByte()); this[24] = ((24).toUByte()); this[25] = ((24).toUByte()); this[26] = ((24).toUByte()); this[27] = ((30).toUByte()); this[28] = ((30).toUByte()); this[29] = ((30).toUByte()); this[30] = ((40).toUByte()); this[31] = ((40).toUByte()); this[32] = ((40).toUByte()); this[33] = ((18).toUByte()); this[34] = ((18).toUByte()); this[35] = ((18).toUByte()); this[36] = ((0).toUByte()) }; this[1] = Array40UByteAlloc { this[0] = ((12).toUByte()); this[1] = ((12).toUByte()); this[2] = ((12).toUByte()); this[3] = ((4).toUByte()); this[4] = ((4).toUByte()); this[5] = ((4).toUByte()); this[6] = ((8).toUByte()); this[7] = ((8).toUByte()); this[8] = ((8).toUByte()); this[9] = ((12).toUByte()); this[10] = ((12).toUByte()); this[11] = ((12).toUByte()); this[12] = ((16).toUByte()); this[13] = ((16).toUByte()); this[14] = ((16).toUByte()); this[15] = ((20).toUByte()); this[16] = ((20).toUByte()); this[17] = ((20).toUByte()); this[18] = ((24).toUByte()); this[19] = ((24).toUByte()); this[20] = ((24).toUByte()); this[21] = ((28).toUByte()); this[22] = ((28).toUByte()); this[23] = ((28).toUByte()); this[24] = ((36).toUByte()); this[25] = ((36).toUByte()); this[26] = ((36).toUByte()); this[27] = ((2).toUByte()); this[28] = ((2).toUByte()); this[29] = ((2).toUByte()); this[30] = ((2).toUByte()); this[31] = ((2).toUByte()); this[32] = ((2).toUByte()); this[33] = ((2).toUByte()); this[34] = ((2).toUByte()); this[35] = ((2).toUByte()); this[36] = ((26).toUByte()); this[37] = ((26).toUByte()); this[38] = ((26).toUByte()); this[39] = ((0).toUByte()) }; this[2] = Array40UByteAlloc { this[0] = ((6).toUByte()); this[1] = ((6).toUByte()); this[2] = ((6).toUByte()); this[3] = ((6).toUByte()); this[4] = ((6).toUByte()); this[5] = ((6).toUByte()); this[6] = ((6).toUByte()); this[7] = ((6).toUByte()); this[8] = ((6).toUByte()); this[9] = ((6).toUByte()); this[10] = ((6).toUByte()); this[11] = ((6).toUByte()); this[12] = ((8).toUByte()); this[13] = ((8).toUByte()); this[14] = ((8).toUByte()); this[15] = ((10).toUByte()); this[16] = ((10).toUByte()); this[17] = ((10).toUByte()); this[18] = ((14).toUByte()); this[19] = ((14).toUByte()); this[20] = ((14).toUByte()); this[21] = ((18).toUByte()); this[22] = ((18).toUByte()); this[23] = ((18).toUByte()); this[24] = ((26).toUByte()); this[25] = ((26).toUByte()); this[26] = ((26).toUByte()); this[27] = ((32).toUByte()); this[28] = ((32).toUByte()); this[29] = ((32).toUByte()); this[30] = ((42).toUByte()); this[31] = ((42).toUByte()); this[32] = ((42).toUByte()); this[33] = ((18).toUByte()); this[34] = ((18).toUByte()); this[35] = ((18).toUByte()); this[36] = ((0).toUByte()) }; this[3] = Array40UByteAlloc { this[0] = ((6).toUByte()); this[1] = ((6).toUByte()); this[2] = ((6).toUByte()); this[3] = ((6).toUByte()); this[4] = ((6).toUByte()); this[5] = ((6).toUByte()); this[6] = ((6).toUByte()); this[7] = ((6).toUByte()); this[8] = ((6).toUByte()); this[9] = ((8).toUByte()); this[10] = ((8).toUByte()); this[11] = ((8).toUByte()); this[12] = ((10).toUByte()); this[13] = ((10).toUByte()); this[14] = ((10).toUByte()); this[15] = ((12).toUByte()); this[16] = ((12).toUByte()); this[17] = ((12).toUByte()); this[18] = ((14).toUByte()); this[19] = ((14).toUByte()); this[20] = ((14).toUByte()); this[21] = ((18).toUByte()); this[22] = ((18).toUByte()); this[23] = ((18).toUByte()); this[24] = ((24).toUByte()); this[25] = ((24).toUByte()); this[26] = ((24).toUByte()); this[27] = ((32).toUByte()); this[28] = ((32).toUByte()); this[29] = ((32).toUByte()); this[30] = ((44).toUByte()); this[31] = ((44).toUByte()); this[32] = ((44).toUByte()); this[33] = ((12).toUByte()); this[34] = ((12).toUByte()); this[35] = ((12).toUByte()); this[36] = ((0).toUByte()) }; this[4] = Array40UByteAlloc { this[0] = ((6).toUByte()); this[1] = ((6).toUByte()); this[2] = ((6).toUByte()); this[3] = ((6).toUByte()); this[4] = ((6).toUByte()); this[5] = ((6).toUByte()); this[6] = ((6).toUByte()); this[7] = ((6).toUByte()); this[8] = ((6).toUByte()); this[9] = ((8).toUByte()); this[10] = ((8).toUByte()); this[11] = ((8).toUByte()); this[12] = ((10).toUByte()); this[13] = ((10).toUByte()); this[14] = ((10).toUByte()); this[15] = ((12).toUByte()); this[16] = ((12).toUByte()); this[17] = ((12).toUByte()); this[18] = ((14).toUByte()); this[19] = ((14).toUByte()); this[20] = ((14).toUByte()); this[21] = ((18).toUByte()); this[22] = ((18).toUByte()); this[23] = ((18).toUByte()); this[24] = ((24).toUByte()); this[25] = ((24).toUByte()); this[26] = ((24).toUByte()); this[27] = ((30).toUByte()); this[28] = ((30).toUByte()); this[29] = ((30).toUByte()); this[30] = ((40).toUByte()); this[31] = ((40).toUByte()); this[32] = ((40).toUByte()); this[33] = ((18).toUByte()); this[34] = ((18).toUByte()); this[35] = ((18).toUByte()); this[36] = ((0).toUByte()) }; this[5] = Array40UByteAlloc { this[0] = ((4).toUByte()); this[1] = ((4).toUByte()); this[2] = ((4).toUByte()); this[3] = ((4).toUByte()); this[4] = ((4).toUByte()); this[5] = ((4).toUByte()); this[6] = ((6).toUByte()); this[7] = ((6).toUByte()); this[8] = ((4).toUByte()); this[9] = ((4).toUByte()); this[10] = ((4).toUByte()); this[11] = ((6).toUByte()); this[12] = ((6).toUByte()); this[13] = ((6).toUByte()); this[14] = ((8).toUByte()); this[15] = ((8).toUByte()); this[16] = ((8).toUByte()); this[17] = ((10).toUByte()); this[18] = ((10).toUByte()); this[19] = ((10).toUByte()); this[20] = ((12).toUByte()); this[21] = ((12).toUByte()); this[22] = ((12).toUByte()); this[23] = ((14).toUByte()); this[24] = ((14).toUByte()); this[25] = ((14).toUByte()); this[26] = ((18).toUByte()); this[27] = ((18).toUByte()); this[28] = ((18).toUByte()); this[29] = ((22).toUByte()); this[30] = ((22).toUByte()); this[31] = ((22).toUByte()); this[32] = ((30).toUByte()); this[33] = ((30).toUByte()); this[34] = ((30).toUByte()); this[35] = ((56).toUByte()); this[36] = ((56).toUByte()); this[37] = ((56).toUByte()); this[38] = ((0).toUByte()) }; this[6] = Array40UByteAlloc { this[0] = ((4).toUByte()); this[1] = ((4).toUByte()); this[2] = ((4).toUByte()); this[3] = ((4).toUByte()); this[4] = ((4).toUByte()); this[5] = ((4).toUByte()); this[6] = ((6).toUByte()); this[7] = ((6).toUByte()); this[8] = ((4).toUByte()); this[9] = ((4).toUByte()); this[10] = ((4).toUByte()); this[11] = ((6).toUByte()); this[12] = ((6).toUByte()); this[13] = ((6).toUByte()); this[14] = ((6).toUByte()); this[15] = ((6).toUByte()); this[16] = ((6).toUByte()); this[17] = ((10).toUByte()); this[18] = ((10).toUByte()); this[19] = ((10).toUByte()); this[20] = ((12).toUByte()); this[21] = ((12).toUByte()); this[22] = ((12).toUByte()); this[23] = ((14).toUByte()); this[24] = ((14).toUByte()); this[25] = ((14).toUByte()); this[26] = ((16).toUByte()); this[27] = ((16).toUByte()); this[28] = ((16).toUByte()); this[29] = ((20).toUByte()); this[30] = ((20).toUByte()); this[31] = ((20).toUByte()); this[32] = ((26).toUByte()); this[33] = ((26).toUByte()); this[34] = ((26).toUByte()); this[35] = ((66).toUByte()); this[36] = ((66).toUByte()); this[37] = ((66).toUByte()); this[38] = ((0).toUByte()) }; this[7] = Array40UByteAlloc { this[0] = ((4).toUByte()); this[1] = ((4).toUByte()); this[2] = ((4).toUByte()); this[3] = ((4).toUByte()); this[4] = ((4).toUByte()); this[5] = ((4).toUByte()); this[6] = ((6).toUByte()); this[7] = ((6).toUByte()); this[8] = ((4).toUByte()); this[9] = ((4).toUByte()); this[10] = ((4).toUByte()); this[11] = ((6).toUByte()); this[12] = ((6).toUByte()); this[13] = ((6).toUByte()); this[14] = ((8).toUByte()); this[15] = ((8).toUByte()); this[16] = ((8).toUByte()); this[17] = ((12).toUByte()); this[18] = ((12).toUByte()); this[19] = ((12).toUByte()); this[20] = ((16).toUByte()); this[21] = ((16).toUByte()); this[22] = ((16).toUByte()); this[23] = ((20).toUByte()); this[24] = ((20).toUByte()); this[25] = ((20).toUByte()); this[26] = ((26).toUByte()); this[27] = ((26).toUByte()); this[28] = ((26).toUByte()); this[29] = ((34).toUByte()); this[30] = ((34).toUByte()); this[31] = ((34).toUByte()); this[32] = ((42).toUByte()); this[33] = ((42).toUByte()); this[34] = ((42).toUByte()); this[35] = ((12).toUByte()); this[36] = ((12).toUByte()); this[37] = ((12).toUByte()); this[38] = ((0).toUByte()) } } }()
    private var __STATIC_L3_ldexp_q2_g_expfrac: Array4Float = { Array4FloatAlloc { this[0] = 9.31322575E-10f; this[1] = 7.83145814E-10f; this[2] = 6.58544508E-10f; this[3] = 5.53767716E-10f } }()
    private var __STATIC_L3_decode_scalefactors_g_scf_partitions: Array3Array28UByte = { Array3Array28UByteAlloc { this[0] = Array28UByteAlloc { this[0] = ((6).toUByte()); this[1] = ((5).toUByte()); this[2] = ((5).toUByte()); this[3] = ((5).toUByte()); this[4] = ((6).toUByte()); this[5] = ((5).toUByte()); this[6] = ((5).toUByte()); this[7] = ((5).toUByte()); this[8] = ((6).toUByte()); this[9] = ((5).toUByte()); this[10] = ((7).toUByte()); this[11] = ((3).toUByte()); this[12] = ((11).toUByte()); this[13] = ((10).toUByte()); this[14] = ((0).toUByte()); this[15] = ((0).toUByte()); this[16] = ((7).toUByte()); this[17] = ((7).toUByte()); this[18] = ((7).toUByte()); this[19] = ((0).toUByte()); this[20] = ((6).toUByte()); this[21] = ((6).toUByte()); this[22] = ((6).toUByte()); this[23] = ((3).toUByte()); this[24] = ((8).toUByte()); this[25] = ((8).toUByte()); this[26] = ((5).toUByte()); this[27] = ((0).toUByte()) }; this[1] = Array28UByteAlloc { this[0] = ((8).toUByte()); this[1] = ((9).toUByte()); this[2] = ((6).toUByte()); this[3] = ((12).toUByte()); this[4] = ((6).toUByte()); this[5] = ((9).toUByte()); this[6] = ((9).toUByte()); this[7] = ((9).toUByte()); this[8] = ((6).toUByte()); this[9] = ((9).toUByte()); this[10] = ((12).toUByte()); this[11] = ((6).toUByte()); this[12] = ((15).toUByte()); this[13] = ((18).toUByte()); this[14] = ((0).toUByte()); this[15] = ((0).toUByte()); this[16] = ((6).toUByte()); this[17] = ((15).toUByte()); this[18] = ((12).toUByte()); this[19] = ((0).toUByte()); this[20] = ((6).toUByte()); this[21] = ((12).toUByte()); this[22] = ((9).toUByte()); this[23] = ((6).toUByte()); this[24] = ((6).toUByte()); this[25] = ((18).toUByte()); this[26] = ((9).toUByte()); this[27] = ((0).toUByte()) }; this[2] = Array28UByteAlloc { this[0] = ((9).toUByte()); this[1] = ((9).toUByte()); this[2] = ((6).toUByte()); this[3] = ((12).toUByte()); this[4] = ((9).toUByte()); this[5] = ((9).toUByte()); this[6] = ((9).toUByte()); this[7] = ((9).toUByte()); this[8] = ((9).toUByte()); this[9] = ((9).toUByte()); this[10] = ((12).toUByte()); this[11] = ((6).toUByte()); this[12] = ((18).toUByte()); this[13] = ((18).toUByte()); this[14] = ((0).toUByte()); this[15] = ((0).toUByte()); this[16] = ((12).toUByte()); this[17] = ((12).toUByte()); this[18] = ((12).toUByte()); this[19] = ((0).toUByte()); this[20] = ((12).toUByte()); this[21] = ((9).toUByte()); this[22] = ((9).toUByte()); this[23] = ((6).toUByte()); this[24] = ((15).toUByte()); this[25] = ((12).toUByte()); this[26] = ((9).toUByte()); this[27] = ((0).toUByte()) } } }()
    private var __STATIC_L3_decode_scalefactors_g_scfc_decode: Array16UByte = { Array16UByteAlloc { this[0] = ((0).toUByte()); this[1] = ((1).toUByte()); this[2] = ((2).toUByte()); this[3] = ((3).toUByte()); this[4] = ((12).toUByte()); this[5] = ((5).toUByte()); this[6] = ((6).toUByte()); this[7] = ((7).toUByte()); this[8] = ((9).toUByte()); this[9] = ((10).toUByte()); this[10] = ((11).toUByte()); this[11] = ((13).toUByte()); this[12] = ((14).toUByte()); this[13] = ((15).toUByte()); this[14] = ((18).toUByte()); this[15] = ((19).toUByte()) } }()
    private var __STATIC_L3_decode_scalefactors_g_mod: Array24UByte = { Array24UByteAlloc { this[0] = ((5).toUByte()); this[1] = ((5).toUByte()); this[2] = ((4).toUByte()); this[3] = ((4).toUByte()); this[4] = ((5).toUByte()); this[5] = ((5).toUByte()); this[6] = ((4).toUByte()); this[7] = ((1).toUByte()); this[8] = ((4).toUByte()); this[9] = ((3).toUByte()); this[10] = ((1).toUByte()); this[11] = ((1).toUByte()); this[12] = ((5).toUByte()); this[13] = ((6).toUByte()); this[14] = ((6).toUByte()); this[15] = ((1).toUByte()); this[16] = ((4).toUByte()); this[17] = ((4).toUByte()); this[18] = ((4).toUByte()); this[19] = ((1).toUByte()); this[20] = ((4).toUByte()); this[21] = ((3).toUByte()); this[22] = ((1).toUByte()); this[23] = ((1).toUByte()) } }()
    private var __STATIC_L3_decode_scalefactors_g_preamp: Array10UByte = { Array10UByteAlloc { this[0] = ((1).toUByte()); this[1] = ((1).toUByte()); this[2] = ((1).toUByte()); this[3] = ((1).toUByte()); this[4] = ((2).toUByte()); this[5] = ((2).toUByte()); this[6] = ((3).toUByte()); this[7] = ((3).toUByte()); this[8] = ((3).toUByte()); this[9] = ((2).toUByte()) } }()
    private var __STATIC_L3_huffman_tabs: CPointer<Short> = { fixedArrayOfShort(2164) { this[0] = ((0).toShort()); this[1] = ((0).toShort()); this[2] = ((0).toShort()); this[3] = ((0).toShort()); this[4] = ((0).toShort()); this[5] = ((0).toShort()); this[6] = ((0).toShort()); this[7] = ((0).toShort()); this[8] = ((0).toShort()); this[9] = ((0).toShort()); this[10] = ((0).toShort()); this[11] = ((0).toShort()); this[12] = ((0).toShort()); this[13] = ((0).toShort()); this[14] = ((0).toShort()); this[15] = ((0).toShort()); this[16] = ((0).toShort()); this[17] = ((0).toShort()); this[18] = ((0).toShort()); this[19] = ((0).toShort()); this[20] = ((0).toShort()); this[21] = ((0).toShort()); this[22] = ((0).toShort()); this[23] = ((0).toShort()); this[24] = ((0).toShort()); this[25] = ((0).toShort()); this[26] = ((0).toShort()); this[27] = ((0).toShort()); this[28] = ((0).toShort()); this[29] = ((0).toShort()); this[30] = ((0).toShort()); this[31] = ((0).toShort()); this[32] = ((785).toShort()); this[33] = ((785).toShort()); this[34] = ((785).toShort()); this[35] = ((785).toShort()); this[36] = ((784).toShort()); this[37] = ((784).toShort()); this[38] = ((784).toShort()); this[39] = ((784).toShort()); this[40] = ((513).toShort()); this[41] = ((513).toShort()); this[42] = ((513).toShort()); this[43] = ((513).toShort()); this[44] = ((513).toShort()); this[45] = ((513).toShort()); this[46] = ((513).toShort()); this[47] = ((513).toShort()); this[48] = ((256).toShort()); this[49] = ((256).toShort()); this[50] = ((256).toShort()); this[51] = ((256).toShort()); this[52] = ((256).toShort()); this[53] = ((256).toShort()); this[54] = ((256).toShort()); this[55] = ((256).toShort()); this[56] = ((256).toShort()); this[57] = ((256).toShort()); this[58] = ((256).toShort()); this[59] = ((256).toShort()); this[60] = ((256).toShort()); this[61] = ((256).toShort()); this[62] = ((256).toShort()); this[63] = ((256).toShort()); this[64] = ((-255L).toShort()); this[65] = ((1313).toShort()); this[66] = ((1298).toShort()); this[67] = ((1282).toShort()); this[68] = ((785).toShort()); this[69] = ((785).toShort()); this[70] = ((785).toShort()); this[71] = ((785).toShort()); this[72] = ((784).toShort()); this[73] = ((784).toShort()); this[74] = ((784).toShort()); this[75] = ((784).toShort()); this[76] = ((769).toShort()); this[77] = ((769).toShort()); this[78] = ((769).toShort()); this[79] = ((769).toShort()); this[80] = ((256).toShort()); this[81] = ((256).toShort()); this[82] = ((256).toShort()); this[83] = ((256).toShort()); this[84] = ((256).toShort()); this[85] = ((256).toShort()); this[86] = ((256).toShort()); this[87] = ((256).toShort()); this[88] = ((256).toShort()); this[89] = ((256).toShort()); this[90] = ((256).toShort()); this[91] = ((256).toShort()); this[92] = ((256).toShort()); this[93] = ((256).toShort()); this[94] = ((256).toShort()); this[95] = ((256).toShort()); this[96] = ((290).toShort()); this[97] = ((288).toShort()); this[98] = ((-255L).toShort()); this[99] = ((1313).toShort()); this[100] = ((1298).toShort()); this[101] = ((1282).toShort()); this[102] = ((769).toShort()); this[103] = ((769).toShort()); this[104] = ((769).toShort()); this[105] = ((769).toShort()); this[106] = ((529).toShort()); this[107] = ((529).toShort()); this[108] = ((529).toShort()); this[109] = ((529).toShort()); this[110] = ((529).toShort()); this[111] = ((529).toShort()); this[112] = ((529).toShort()); this[113] = ((529).toShort()); this[114] = ((528).toShort()); this[115] = ((528).toShort()); this[116] = ((528).toShort()); this[117] = ((528).toShort()); this[118] = ((528).toShort()); this[119] = ((528).toShort()); this[120] = ((528).toShort()); this[121] = ((528).toShort()); this[122] = ((512).toShort()); this[123] = ((512).toShort()); this[124] = ((512).toShort()); this[125] = ((512).toShort()); this[126] = ((512).toShort()); this[127] = ((512).toShort()); this[128] = ((512).toShort()); this[129] = ((512).toShort()); this[130] = ((290).toShort()); this[131] = ((288).toShort()); this[132] = ((-253L).toShort()); this[133] = ((-318L).toShort()); this[134] = ((-351L).toShort()); this[135] = ((-367L).toShort()); this[136] = ((785).toShort()); this[137] = ((785).toShort()); this[138] = ((785).toShort()); this[139] = ((785).toShort()); this[140] = ((784).toShort()); this[141] = ((784).toShort()); this[142] = ((784).toShort()); this[143] = ((784).toShort()); this[144] = ((769).toShort()); this[145] = ((769).toShort()); this[146] = ((769).toShort()); this[147] = ((769).toShort()); this[148] = ((256).toShort()); this[149] = ((256).toShort()); this[150] = ((256).toShort()); this[151] = ((256).toShort()); this[152] = ((256).toShort()); this[153] = ((256).toShort()); this[154] = ((256).toShort()); this[155] = ((256).toShort()); this[156] = ((256).toShort()); this[157] = ((256).toShort()); this[158] = ((256).toShort()); this[159] = ((256).toShort()); this[160] = ((256).toShort()); this[161] = ((256).toShort()); this[162] = ((256).toShort()); this[163] = ((256).toShort()); this[164] = ((819).toShort()); this[165] = ((818).toShort()); this[166] = ((547).toShort()); this[167] = ((547).toShort()); this[168] = ((275).toShort()); this[169] = ((275).toShort()); this[170] = ((275).toShort()); this[171] = ((275).toShort()); this[172] = ((561).toShort()); this[173] = ((560).toShort()); this[174] = ((515).toShort()); this[175] = ((546).toShort()); this[176] = ((289).toShort()); this[177] = ((274).toShort()); this[178] = ((288).toShort()); this[179] = ((258).toShort()); this[180] = ((-254L).toShort()); this[181] = ((-287L).toShort()); this[182] = ((1329).toShort()); this[183] = ((1299).toShort()); this[184] = ((1314).toShort()); this[185] = ((1312).toShort()); this[186] = ((1057).toShort()); this[187] = ((1057).toShort()); this[188] = ((1042).toShort()); this[189] = ((1042).toShort()); this[190] = ((1026).toShort()); this[191] = ((1026).toShort()); this[192] = ((784).toShort()); this[193] = ((784).toShort()); this[194] = ((784).toShort()); this[195] = ((784).toShort()); this[196] = ((529).toShort()); this[197] = ((529).toShort()); this[198] = ((529).toShort()); this[199] = ((529).toShort()); this[200] = ((529).toShort()); this[201] = ((529).toShort()); this[202] = ((529).toShort()); this[203] = ((529).toShort()); this[204] = ((769).toShort()); this[205] = ((769).toShort()); this[206] = ((769).toShort()); this[207] = ((769).toShort()); this[208] = ((768).toShort()); this[209] = ((768).toShort()); this[210] = ((768).toShort()); this[211] = ((768).toShort()); this[212] = ((563).toShort()); this[213] = ((560).toShort()); this[214] = ((306).toShort()); this[215] = ((306).toShort()); this[216] = ((291).toShort()); this[217] = ((259).toShort()); this[218] = ((-252L).toShort()); this[219] = ((-413L).toShort()); this[220] = ((-477L).toShort()); this[221] = ((-542L).toShort()); this[222] = ((1298).toShort()); this[223] = ((-575L).toShort()); this[224] = ((1041).toShort()); this[225] = ((1041).toShort()); this[226] = ((784).toShort()); this[227] = ((784).toShort()); this[228] = ((784).toShort()); this[229] = ((784).toShort()); this[230] = ((769).toShort()); this[231] = ((769).toShort()); this[232] = ((769).toShort()); this[233] = ((769).toShort()); this[234] = ((256).toShort()); this[235] = ((256).toShort()); this[236] = ((256).toShort()); this[237] = ((256).toShort()); this[238] = ((256).toShort()); this[239] = ((256).toShort()); this[240] = ((256).toShort()); this[241] = ((256).toShort()); this[242] = ((256).toShort()); this[243] = ((256).toShort()); this[244] = ((256).toShort()); this[245] = ((256).toShort()); this[246] = ((256).toShort()); this[247] = ((256).toShort()); this[248] = ((256).toShort()); this[249] = ((256).toShort()); this[250] = ((-383L).toShort()); this[251] = ((-399L).toShort()); this[252] = ((1107).toShort()); this[253] = ((1092).toShort()); this[254] = ((1106).toShort()); this[255] = ((1061).toShort()); this[256] = ((849).toShort()); this[257] = ((849).toShort()); this[258] = ((789).toShort()); this[259] = ((789).toShort()); this[260] = ((1104).toShort()); this[261] = ((1091).toShort()); this[262] = ((773).toShort()); this[263] = ((773).toShort()); this[264] = ((1076).toShort()); this[265] = ((1075).toShort()); this[266] = ((341).toShort()); this[267] = ((340).toShort()); this[268] = ((325).toShort()); this[269] = ((309).toShort()); this[270] = ((834).toShort()); this[271] = ((804).toShort()); this[272] = ((577).toShort()); this[273] = ((577).toShort()); this[274] = ((532).toShort()); this[275] = ((532).toShort()); this[276] = ((516).toShort()); this[277] = ((516).toShort()); this[278] = ((832).toShort()); this[279] = ((818).toShort()); this[280] = ((803).toShort()); this[281] = ((816).toShort()); this[282] = ((561).toShort()); this[283] = ((561).toShort()); this[284] = ((531).toShort()); this[285] = ((531).toShort()); this[286] = ((515).toShort()); this[287] = ((546).toShort()); this[288] = ((289).toShort()); this[289] = ((289).toShort()); this[290] = ((288).toShort()); this[291] = ((258).toShort()); this[292] = ((-252L).toShort()); this[293] = ((-429L).toShort()); this[294] = ((-493L).toShort()); this[295] = ((-559L).toShort()); this[296] = ((1057).toShort()); this[297] = ((1057).toShort()); this[298] = ((1042).toShort()); this[299] = ((1042).toShort()); this[300] = ((529).toShort()); this[301] = ((529).toShort()); this[302] = ((529).toShort()); this[303] = ((529).toShort()); this[304] = ((529).toShort()); this[305] = ((529).toShort()); this[306] = ((529).toShort()); this[307] = ((529).toShort()); this[308] = ((784).toShort()); this[309] = ((784).toShort()); this[310] = ((784).toShort()); this[311] = ((784).toShort()); this[312] = ((769).toShort()); this[313] = ((769).toShort()); this[314] = ((769).toShort()); this[315] = ((769).toShort()); this[316] = ((512).toShort()); this[317] = ((512).toShort()); this[318] = ((512).toShort()); this[319] = ((512).toShort()); this[320] = ((512).toShort()); this[321] = ((512).toShort()); this[322] = ((512).toShort()); this[323] = ((512).toShort()); this[324] = ((-382L).toShort()); this[325] = ((1077).toShort()); this[326] = ((-415L).toShort()); this[327] = ((1106).toShort()); this[328] = ((1061).toShort()); this[329] = ((1104).toShort()); this[330] = ((849).toShort()); this[331] = ((849).toShort()); this[332] = ((789).toShort()); this[333] = ((789).toShort()); this[334] = ((1091).toShort()); this[335] = ((1076).toShort()); this[336] = ((1029).toShort()); this[337] = ((1075).toShort()); this[338] = ((834).toShort()); this[339] = ((834).toShort()); this[340] = ((597).toShort()); this[341] = ((581).toShort()); this[342] = ((340).toShort()); this[343] = ((340).toShort()); this[344] = ((339).toShort()); this[345] = ((324).toShort()); this[346] = ((804).toShort()); this[347] = ((833).toShort()); this[348] = ((532).toShort()); this[349] = ((532).toShort()); this[350] = ((832).toShort()); this[351] = ((772).toShort()); this[352] = ((818).toShort()); this[353] = ((803).toShort()); this[354] = ((817).toShort()); this[355] = ((787).toShort()); this[356] = ((816).toShort()); this[357] = ((771).toShort()); this[358] = ((290).toShort()); this[359] = ((290).toShort()); this[360] = ((290).toShort()); this[361] = ((290).toShort()); this[362] = ((288).toShort()); this[363] = ((258).toShort()); this[364] = ((-253L).toShort()); this[365] = ((-349L).toShort()); this[366] = ((-414L).toShort()); this[367] = ((-447L).toShort()); this[368] = ((-463L).toShort()); this[369] = ((1329).toShort()); this[370] = ((1299).toShort()); this[371] = ((-479L).toShort()); this[372] = ((1314).toShort()); this[373] = ((1312).toShort()); this[374] = ((1057).toShort()); this[375] = ((1057).toShort()); this[376] = ((1042).toShort()); this[377] = ((1042).toShort()); this[378] = ((1026).toShort()); this[379] = ((1026).toShort()); this[380] = ((785).toShort()); this[381] = ((785).toShort()); this[382] = ((785).toShort()); this[383] = ((785).toShort()); this[384] = ((784).toShort()); this[385] = ((784).toShort()); this[386] = ((784).toShort()); this[387] = ((784).toShort()); this[388] = ((769).toShort()); this[389] = ((769).toShort()); this[390] = ((769).toShort()); this[391] = ((769).toShort()); this[392] = ((768).toShort()); this[393] = ((768).toShort()); this[394] = ((768).toShort()); this[395] = ((768).toShort()); this[396] = ((-319L).toShort()); this[397] = ((851).toShort()); this[398] = ((821).toShort()); this[399] = ((-335L).toShort()); this[400] = ((836).toShort()); this[401] = ((850).toShort()); this[402] = ((805).toShort()); this[403] = ((849).toShort()); this[404] = ((341).toShort()); this[405] = ((340).toShort()); this[406] = ((325).toShort()); this[407] = ((336).toShort()); this[408] = ((533).toShort()); this[409] = ((533).toShort()); this[410] = ((579).toShort()); this[411] = ((579).toShort()); this[412] = ((564).toShort()); this[413] = ((564).toShort()); this[414] = ((773).toShort()); this[415] = ((832).toShort()); this[416] = ((578).toShort()); this[417] = ((548).toShort()); this[418] = ((563).toShort()); this[419] = ((516).toShort()); this[420] = ((321).toShort()); this[421] = ((276).toShort()); this[422] = ((306).toShort()); this[423] = ((291).toShort()); this[424] = ((304).toShort()); this[425] = ((259).toShort()); this[426] = ((-251L).toShort()); this[427] = ((-572L).toShort()); this[428] = ((-733L).toShort()); this[429] = ((-830L).toShort()); this[430] = ((-863L).toShort()); this[431] = ((-879L).toShort()); this[432] = ((1041).toShort()); this[433] = ((1041).toShort()); this[434] = ((784).toShort()); this[435] = ((784).toShort()); this[436] = ((784).toShort()); this[437] = ((784).toShort()); this[438] = ((769).toShort()); this[439] = ((769).toShort()); this[440] = ((769).toShort()); this[441] = ((769).toShort()); this[442] = ((256).toShort()); this[443] = ((256).toShort()); this[444] = ((256).toShort()); this[445] = ((256).toShort()); this[446] = ((256).toShort()); this[447] = ((256).toShort()); this[448] = ((256).toShort()); this[449] = ((256).toShort()); this[450] = ((256).toShort()); this[451] = ((256).toShort()); this[452] = ((256).toShort()); this[453] = ((256).toShort()); this[454] = ((256).toShort()); this[455] = ((256).toShort()); this[456] = ((256).toShort()); this[457] = ((256).toShort()); this[458] = ((-511L).toShort()); this[459] = ((-527L).toShort()); this[460] = ((-543L).toShort()); this[461] = ((1396).toShort()); this[462] = ((1351).toShort()); this[463] = ((1381).toShort()); this[464] = ((1366).toShort()); this[465] = ((1395).toShort()); this[466] = ((1335).toShort()); this[467] = ((1380).toShort()); this[468] = ((-559L).toShort()); this[469] = ((1334).toShort()); this[470] = ((1138).toShort()); this[471] = ((1138).toShort()); this[472] = ((1063).toShort()); this[473] = ((1063).toShort()); this[474] = ((1350).toShort()); this[475] = ((1392).toShort()); this[476] = ((1031).toShort()); this[477] = ((1031).toShort()); this[478] = ((1062).toShort()); this[479] = ((1062).toShort()); this[480] = ((1364).toShort()); this[481] = ((1363).toShort()); this[482] = ((1120).toShort()); this[483] = ((1120).toShort()); this[484] = ((1333).toShort()); this[485] = ((1348).toShort()); this[486] = ((881).toShort()); this[487] = ((881).toShort()); this[488] = ((881).toShort()); this[489] = ((881).toShort()); this[490] = ((375).toShort()); this[491] = ((374).toShort()); this[492] = ((359).toShort()); this[493] = ((373).toShort()); this[494] = ((343).toShort()); this[495] = ((358).toShort()); this[496] = ((341).toShort()); this[497] = ((325).toShort()); this[498] = ((791).toShort()); this[499] = ((791).toShort()); this[500] = ((1123).toShort()); this[501] = ((1122).toShort()); this[502] = ((-703L).toShort()); this[503] = ((1105).toShort()); this[504] = ((1045).toShort()); this[505] = ((-719L).toShort()); this[506] = ((865).toShort()); this[507] = ((865).toShort()); this[508] = ((790).toShort()); this[509] = ((790).toShort()); this[510] = ((774).toShort()); this[511] = ((774).toShort()); this[512] = ((1104).toShort()); this[513] = ((1029).toShort()); this[514] = ((338).toShort()); this[515] = ((293).toShort()); this[516] = ((323).toShort()); this[517] = ((308).toShort()); this[518] = ((-799L).toShort()); this[519] = ((-815L).toShort()); this[520] = ((833).toShort()); this[521] = ((788).toShort()); this[522] = ((772).toShort()); this[523] = ((818).toShort()); this[524] = ((803).toShort()); this[525] = ((816).toShort()); this[526] = ((322).toShort()); this[527] = ((292).toShort()); this[528] = ((307).toShort()); this[529] = ((320).toShort()); this[530] = ((561).toShort()); this[531] = ((531).toShort()); this[532] = ((515).toShort()); this[533] = ((546).toShort()); this[534] = ((289).toShort()); this[535] = ((274).toShort()); this[536] = ((288).toShort()); this[537] = ((258).toShort()); this[538] = ((-251L).toShort()); this[539] = ((-525L).toShort()); this[540] = ((-605L).toShort()); this[541] = ((-685L).toShort()); this[542] = ((-765L).toShort()); this[543] = ((-831L).toShort()); this[544] = ((-846L).toShort()); this[545] = ((1298).toShort()); this[546] = ((1057).toShort()); this[547] = ((1057).toShort()); this[548] = ((1312).toShort()); this[549] = ((1282).toShort()); this[550] = ((785).toShort()); this[551] = ((785).toShort()); this[552] = ((785).toShort()); this[553] = ((785).toShort()); this[554] = ((784).toShort()); this[555] = ((784).toShort()); this[556] = ((784).toShort()); this[557] = ((784).toShort()); this[558] = ((769).toShort()); this[559] = ((769).toShort()); this[560] = ((769).toShort()); this[561] = ((769).toShort()); this[562] = ((512).toShort()); this[563] = ((512).toShort()); this[564] = ((512).toShort()); this[565] = ((512).toShort()); this[566] = ((512).toShort()); this[567] = ((512).toShort()); this[568] = ((512).toShort()); this[569] = ((512).toShort()); this[570] = ((1399).toShort()); this[571] = ((1398).toShort()); this[572] = ((1383).toShort()); this[573] = ((1367).toShort()); this[574] = ((1382).toShort()); this[575] = ((1396).toShort()); this[576] = ((1351).toShort()); this[577] = ((-511L).toShort()); this[578] = ((1381).toShort()); this[579] = ((1366).toShort()); this[580] = ((1139).toShort()); this[581] = ((1139).toShort()); this[582] = ((1079).toShort()); this[583] = ((1079).toShort()); this[584] = ((1124).toShort()); this[585] = ((1124).toShort()); this[586] = ((1364).toShort()); this[587] = ((1349).toShort()); this[588] = ((1363).toShort()); this[589] = ((1333).toShort()); this[590] = ((882).toShort()); this[591] = ((882).toShort()); this[592] = ((882).toShort()); this[593] = ((882).toShort()); this[594] = ((807).toShort()); this[595] = ((807).toShort()); this[596] = ((807).toShort()); this[597] = ((807).toShort()); this[598] = ((1094).toShort()); this[599] = ((1094).toShort()); this[600] = ((1136).toShort()); this[601] = ((1136).toShort()); this[602] = ((373).toShort()); this[603] = ((341).toShort()); this[604] = ((535).toShort()); this[605] = ((535).toShort()); this[606] = ((881).toShort()); this[607] = ((775).toShort()); this[608] = ((867).toShort()); this[609] = ((822).toShort()); this[610] = ((774).toShort()); this[611] = ((-591L).toShort()); this[612] = ((324).toShort()); this[613] = ((338).toShort()); this[614] = ((-671L).toShort()); this[615] = ((849).toShort()); this[616] = ((550).toShort()); this[617] = ((550).toShort()); this[618] = ((866).toShort()); this[619] = ((864).toShort()); this[620] = ((609).toShort()); this[621] = ((609).toShort()); this[622] = ((293).toShort()); this[623] = ((336).toShort()); this[624] = ((534).toShort()); this[625] = ((534).toShort()); this[626] = ((789).toShort()); this[627] = ((835).toShort()); this[628] = ((773).toShort()); this[629] = ((-751L).toShort()); this[630] = ((834).toShort()); this[631] = ((804).toShort()); this[632] = ((308).toShort()); this[633] = ((307).toShort()); this[634] = ((833).toShort()); this[635] = ((788).toShort()); this[636] = ((832).toShort()); this[637] = ((772).toShort()); this[638] = ((562).toShort()); this[639] = ((562).toShort()); this[640] = ((547).toShort()); this[641] = ((547).toShort()); this[642] = ((305).toShort()); this[643] = ((275).toShort()); this[644] = ((560).toShort()); this[645] = ((515).toShort()); this[646] = ((290).toShort()); this[647] = ((290).toShort()); this[648] = ((-252L).toShort()); this[649] = ((-397L).toShort()); this[650] = ((-477L).toShort()); this[651] = ((-557L).toShort()); this[652] = ((-622L).toShort()); this[653] = ((-653L).toShort()); this[654] = ((-719L).toShort()); this[655] = ((-735L).toShort()); this[656] = ((-750L).toShort()); this[657] = ((1329).toShort()); this[658] = ((1299).toShort()); this[659] = ((1314).toShort()); this[660] = ((1057).toShort()); this[661] = ((1057).toShort()); this[662] = ((1042).toShort()); this[663] = ((1042).toShort()); this[664] = ((1312).toShort()); this[665] = ((1282).toShort()); this[666] = ((1024).toShort()); this[667] = ((1024).toShort()); this[668] = ((785).toShort()); this[669] = ((785).toShort()); this[670] = ((785).toShort()); this[671] = ((785).toShort()); this[672] = ((784).toShort()); this[673] = ((784).toShort()); this[674] = ((784).toShort()); this[675] = ((784).toShort()); this[676] = ((769).toShort()); this[677] = ((769).toShort()); this[678] = ((769).toShort()); this[679] = ((769).toShort()); this[680] = ((-383L).toShort()); this[681] = ((1127).toShort()); this[682] = ((1141).toShort()); this[683] = ((1111).toShort()); this[684] = ((1126).toShort()); this[685] = ((1140).toShort()); this[686] = ((1095).toShort()); this[687] = ((1110).toShort()); this[688] = ((869).toShort()); this[689] = ((869).toShort()); this[690] = ((883).toShort()); this[691] = ((883).toShort()); this[692] = ((1079).toShort()); this[693] = ((1109).toShort()); this[694] = ((882).toShort()); this[695] = ((882).toShort()); this[696] = ((375).toShort()); this[697] = ((374).toShort()); this[698] = ((807).toShort()); this[699] = ((868).toShort()); this[700] = ((838).toShort()); this[701] = ((881).toShort()); this[702] = ((791).toShort()); this[703] = ((-463L).toShort()); this[704] = ((867).toShort()); this[705] = ((822).toShort()); this[706] = ((368).toShort()); this[707] = ((263).toShort()); this[708] = ((852).toShort()); this[709] = ((837).toShort()); this[710] = ((836).toShort()); this[711] = ((-543L).toShort()); this[712] = ((610).toShort()); this[713] = ((610).toShort()); this[714] = ((550).toShort()); this[715] = ((550).toShort()); this[716] = ((352).toShort()); this[717] = ((336).toShort()); this[718] = ((534).toShort()); this[719] = ((534).toShort()); this[720] = ((865).toShort()); this[721] = ((774).toShort()); this[722] = ((851).toShort()); this[723] = ((821).toShort()); this[724] = ((850).toShort()); this[725] = ((805).toShort()); this[726] = ((593).toShort()); this[727] = ((533).toShort()); this[728] = ((579).toShort()); this[729] = ((564).toShort()); this[730] = ((773).toShort()); this[731] = ((832).toShort()); this[732] = ((578).toShort()); this[733] = ((578).toShort()); this[734] = ((548).toShort()); this[735] = ((548).toShort()); this[736] = ((577).toShort()); this[737] = ((577).toShort()); this[738] = ((307).toShort()); this[739] = ((276).toShort()); this[740] = ((306).toShort()); this[741] = ((291).toShort()); this[742] = ((516).toShort()); this[743] = ((560).toShort()); this[744] = ((259).toShort()); this[745] = ((259).toShort()); this[746] = ((-250L).toShort()); this[747] = ((-2107L).toShort()); this[748] = ((-2507L).toShort()); this[749] = ((-2764L).toShort()); this[750] = ((-2909L).toShort()); this[751] = ((-2974L).toShort()); this[752] = ((-3007L).toShort()); this[753] = ((-3023L).toShort()); this[754] = ((1041).toShort()); this[755] = ((1041).toShort()); this[756] = ((1040).toShort()); this[757] = ((1040).toShort()); this[758] = ((769).toShort()); this[759] = ((769).toShort()); this[760] = ((769).toShort()); this[761] = ((769).toShort()); this[762] = ((256).toShort()); this[763] = ((256).toShort()); this[764] = ((256).toShort()); this[765] = ((256).toShort()); this[766] = ((256).toShort()); this[767] = ((256).toShort()); this[768] = ((256).toShort()); this[769] = ((256).toShort()); this[770] = ((256).toShort()); this[771] = ((256).toShort()); this[772] = ((256).toShort()); this[773] = ((256).toShort()); this[774] = ((256).toShort()); this[775] = ((256).toShort()); this[776] = ((256).toShort()); this[777] = ((256).toShort()); this[778] = ((-767L).toShort()); this[779] = ((-1052L).toShort()); this[780] = ((-1213L).toShort()); this[781] = ((-1277L).toShort()); this[782] = ((-1358L).toShort()); this[783] = ((-1405L).toShort()); this[784] = ((-1469L).toShort()); this[785] = ((-1535L).toShort()); this[786] = ((-1550L).toShort()); this[787] = ((-1582L).toShort()); this[788] = ((-1614L).toShort()); this[789] = ((-1647L).toShort()); this[790] = ((-1662L).toShort()); this[791] = ((-1694L).toShort()); this[792] = ((-1726L).toShort()); this[793] = ((-1759L).toShort()); this[794] = ((-1774L).toShort()); this[795] = ((-1807L).toShort()); this[796] = ((-1822L).toShort()); this[797] = ((-1854L).toShort()); this[798] = ((-1886L).toShort()); this[799] = ((1565).toShort()); this[800] = ((-1919L).toShort()); this[801] = ((-1935L).toShort()); this[802] = ((-1951L).toShort()); this[803] = ((-1967L).toShort()); this[804] = ((1731).toShort()); this[805] = ((1730).toShort()); this[806] = ((1580).toShort()); this[807] = ((1717).toShort()); this[808] = ((-1983L).toShort()); this[809] = ((1729).toShort()); this[810] = ((1564).toShort()); this[811] = ((-1999L).toShort()); this[812] = ((1548).toShort()); this[813] = ((-2015L).toShort()); this[814] = ((-2031L).toShort()); this[815] = ((1715).toShort()); this[816] = ((1595).toShort()); this[817] = ((-2047L).toShort()); this[818] = ((1714).toShort()); this[819] = ((-2063L).toShort()); this[820] = ((1610).toShort()); this[821] = ((-2079L).toShort()); this[822] = ((1609).toShort()); this[823] = ((-2095L).toShort()); this[824] = ((1323).toShort()); this[825] = ((1323).toShort()); this[826] = ((1457).toShort()); this[827] = ((1457).toShort()); this[828] = ((1307).toShort()); this[829] = ((1307).toShort()); this[830] = ((1712).toShort()); this[831] = ((1547).toShort()); this[832] = ((1641).toShort()); this[833] = ((1700).toShort()); this[834] = ((1699).toShort()); this[835] = ((1594).toShort()); this[836] = ((1685).toShort()); this[837] = ((1625).toShort()); this[838] = ((1442).toShort()); this[839] = ((1442).toShort()); this[840] = ((1322).toShort()); this[841] = ((1322).toShort()); this[842] = ((-780L).toShort()); this[843] = ((-973L).toShort()); this[844] = ((-910L).toShort()); this[845] = ((1279).toShort()); this[846] = ((1278).toShort()); this[847] = ((1277).toShort()); this[848] = ((1262).toShort()); this[849] = ((1276).toShort()); this[850] = ((1261).toShort()); this[851] = ((1275).toShort()); this[852] = ((1215).toShort()); this[853] = ((1260).toShort()); this[854] = ((1229).toShort()); this[855] = ((-959L).toShort()); this[856] = ((974).toShort()); this[857] = ((974).toShort()); this[858] = ((989).toShort()); this[859] = ((989).toShort()); this[860] = ((-943L).toShort()); this[861] = ((735).toShort()); this[862] = ((478).toShort()); this[863] = ((478).toShort()); this[864] = ((495).toShort()); this[865] = ((463).toShort()); this[866] = ((506).toShort()); this[867] = ((414).toShort()); this[868] = ((-1039L).toShort()); this[869] = ((1003).toShort()); this[870] = ((958).toShort()); this[871] = ((1017).toShort()); this[872] = ((927).toShort()); this[873] = ((942).toShort()); this[874] = ((987).toShort()); this[875] = ((957).toShort()); this[876] = ((431).toShort()); this[877] = ((476).toShort()); this[878] = ((1272).toShort()); this[879] = ((1167).toShort()); this[880] = ((1228).toShort()); this[881] = ((-1183L).toShort()); this[882] = ((1256).toShort()); this[883] = ((-1199L).toShort()); this[884] = ((895).toShort()); this[885] = ((895).toShort()); this[886] = ((941).toShort()); this[887] = ((941).toShort()); this[888] = ((1242).toShort()); this[889] = ((1227).toShort()); this[890] = ((1212).toShort()); this[891] = ((1135).toShort()); this[892] = ((1014).toShort()); this[893] = ((1014).toShort()); this[894] = ((490).toShort()); this[895] = ((489).toShort()); this[896] = ((503).toShort()); this[897] = ((487).toShort()); this[898] = ((910).toShort()); this[899] = ((1013).toShort()); this[900] = ((985).toShort()); this[901] = ((925).toShort()); this[902] = ((863).toShort()); this[903] = ((894).toShort()); this[904] = ((970).toShort()); this[905] = ((955).toShort()); this[906] = ((1012).toShort()); this[907] = ((847).toShort()); this[908] = ((-1343L).toShort()); this[909] = ((831).toShort()); this[910] = ((755).toShort()); this[911] = ((755).toShort()); this[912] = ((984).toShort()); this[913] = ((909).toShort()); this[914] = ((428).toShort()); this[915] = ((366).toShort()); this[916] = ((754).toShort()); this[917] = ((559).toShort()); this[918] = ((-1391L).toShort()); this[919] = ((752).toShort()); this[920] = ((486).toShort()); this[921] = ((457).toShort()); this[922] = ((924).toShort()); this[923] = ((997).toShort()); this[924] = ((698).toShort()); this[925] = ((698).toShort()); this[926] = ((983).toShort()); this[927] = ((893).toShort()); this[928] = ((740).toShort()); this[929] = ((740).toShort()); this[930] = ((908).toShort()); this[931] = ((877).toShort()); this[932] = ((739).toShort()); this[933] = ((739).toShort()); this[934] = ((667).toShort()); this[935] = ((667).toShort()); this[936] = ((953).toShort()); this[937] = ((938).toShort()); this[938] = ((497).toShort()); this[939] = ((287).toShort()); this[940] = ((271).toShort()); this[941] = ((271).toShort()); this[942] = ((683).toShort()); this[943] = ((606).toShort()); this[944] = ((590).toShort()); this[945] = ((712).toShort()); this[946] = ((726).toShort()); this[947] = ((574).toShort()); this[948] = ((302).toShort()); this[949] = ((302).toShort()); this[950] = ((738).toShort()); this[951] = ((736).toShort()); this[952] = ((481).toShort()); this[953] = ((286).toShort()); this[954] = ((526).toShort()); this[955] = ((725).toShort()); this[956] = ((605).toShort()); this[957] = ((711).toShort()); this[958] = ((636).toShort()); this[959] = ((724).toShort()); this[960] = ((696).toShort()); this[961] = ((651).toShort()); this[962] = ((589).toShort()); this[963] = ((681).toShort()); this[964] = ((666).toShort()); this[965] = ((710).toShort()); this[966] = ((364).toShort()); this[967] = ((467).toShort()); this[968] = ((573).toShort()); this[969] = ((695).toShort()); this[970] = ((466).toShort()); this[971] = ((466).toShort()); this[972] = ((301).toShort()); this[973] = ((465).toShort()); this[974] = ((379).toShort()); this[975] = ((379).toShort()); this[976] = ((709).toShort()); this[977] = ((604).toShort()); this[978] = ((665).toShort()); this[979] = ((679).toShort()); this[980] = ((316).toShort()); this[981] = ((316).toShort()); this[982] = ((634).toShort()); this[983] = ((633).toShort()); this[984] = ((436).toShort()); this[985] = ((436).toShort()); this[986] = ((464).toShort()); this[987] = ((269).toShort()); this[988] = ((424).toShort()); this[989] = ((394).toShort()); this[990] = ((452).toShort()); this[991] = ((332).toShort()); this[992] = ((438).toShort()); this[993] = ((363).toShort()); this[994] = ((347).toShort()); this[995] = ((408).toShort()); this[996] = ((393).toShort()); this[997] = ((448).toShort()); this[998] = ((331).toShort()); this[999] = ((422).toShort()); this[1000] = ((362).toShort()); this[1001] = ((407).toShort()); this[1002] = ((392).toShort()); this[1003] = ((421).toShort()); this[1004] = ((346).toShort()); this[1005] = ((406).toShort()); this[1006] = ((391).toShort()); this[1007] = ((376).toShort()); this[1008] = ((375).toShort()); this[1009] = ((359).toShort()); this[1010] = ((1441).toShort()); this[1011] = ((1306).toShort()); this[1012] = ((-2367L).toShort()); this[1013] = ((1290).toShort()); this[1014] = ((-2383L).toShort()); this[1015] = ((1337).toShort()); this[1016] = ((-2399L).toShort()); this[1017] = ((-2415L).toShort()); this[1018] = ((1426).toShort()); this[1019] = ((1321).toShort()); this[1020] = ((-2431L).toShort()); this[1021] = ((1411).toShort()); this[1022] = ((1336).toShort()); this[1023] = ((-2447L).toShort()); this[1024] = ((-2463L).toShort()); this[1025] = ((-2479L).toShort()); this[1026] = ((1169).toShort()); this[1027] = ((1169).toShort()); this[1028] = ((1049).toShort()); this[1029] = ((1049).toShort()); this[1030] = ((1424).toShort()); this[1031] = ((1289).toShort()); this[1032] = ((1412).toShort()); this[1033] = ((1352).toShort()); this[1034] = ((1319).toShort()); this[1035] = ((-2495L).toShort()); this[1036] = ((1154).toShort()); this[1037] = ((1154).toShort()); this[1038] = ((1064).toShort()); this[1039] = ((1064).toShort()); this[1040] = ((1153).toShort()); this[1041] = ((1153).toShort()); this[1042] = ((416).toShort()); this[1043] = ((390).toShort()); this[1044] = ((360).toShort()); this[1045] = ((404).toShort()); this[1046] = ((403).toShort()); this[1047] = ((389).toShort()); this[1048] = ((344).toShort()); this[1049] = ((374).toShort()); this[1050] = ((373).toShort()); this[1051] = ((343).toShort()); this[1052] = ((358).toShort()); this[1053] = ((372).toShort()); this[1054] = ((327).toShort()); this[1055] = ((357).toShort()); this[1056] = ((342).toShort()); this[1057] = ((311).toShort()); this[1058] = ((356).toShort()); this[1059] = ((326).toShort()); this[1060] = ((1395).toShort()); this[1061] = ((1394).toShort()); this[1062] = ((1137).toShort()); this[1063] = ((1137).toShort()); this[1064] = ((1047).toShort()); this[1065] = ((1047).toShort()); this[1066] = ((1365).toShort()); this[1067] = ((1392).toShort()); this[1068] = ((1287).toShort()); this[1069] = ((1379).toShort()); this[1070] = ((1334).toShort()); this[1071] = ((1364).toShort()); this[1072] = ((1349).toShort()); this[1073] = ((1378).toShort()); this[1074] = ((1318).toShort()); this[1075] = ((1363).toShort()); this[1076] = ((792).toShort()); this[1077] = ((792).toShort()); this[1078] = ((792).toShort()); this[1079] = ((792).toShort()); this[1080] = ((1152).toShort()); this[1081] = ((1152).toShort()); this[1082] = ((1032).toShort()); this[1083] = ((1032).toShort()); this[1084] = ((1121).toShort()); this[1085] = ((1121).toShort()); this[1086] = ((1046).toShort()); this[1087] = ((1046).toShort()); this[1088] = ((1120).toShort()); this[1089] = ((1120).toShort()); this[1090] = ((1030).toShort()); this[1091] = ((1030).toShort()); this[1092] = ((-2895L).toShort()); this[1093] = ((1106).toShort()); this[1094] = ((1061).toShort()); this[1095] = ((1104).toShort()); this[1096] = ((849).toShort()); this[1097] = ((849).toShort()); this[1098] = ((789).toShort()); this[1099] = ((789).toShort()); this[1100] = ((1091).toShort()); this[1101] = ((1076).toShort()); this[1102] = ((1029).toShort()); this[1103] = ((1090).toShort()); this[1104] = ((1060).toShort()); this[1105] = ((1075).toShort()); this[1106] = ((833).toShort()); this[1107] = ((833).toShort()); this[1108] = ((309).toShort()); this[1109] = ((324).toShort()); this[1110] = ((532).toShort()); this[1111] = ((532).toShort()); this[1112] = ((832).toShort()); this[1113] = ((772).toShort()); this[1114] = ((818).toShort()); this[1115] = ((803).toShort()); this[1116] = ((561).toShort()); this[1117] = ((561).toShort()); this[1118] = ((531).toShort()); this[1119] = ((560).toShort()); this[1120] = ((515).toShort()); this[1121] = ((546).toShort()); this[1122] = ((289).toShort()); this[1123] = ((274).toShort()); this[1124] = ((288).toShort()); this[1125] = ((258).toShort()); this[1126] = ((-250L).toShort()); this[1127] = ((-1179L).toShort()); this[1128] = ((-1579L).toShort()); this[1129] = ((-1836L).toShort()); this[1130] = ((-1996L).toShort()); this[1131] = ((-2124L).toShort()); this[1132] = ((-2253L).toShort()); this[1133] = ((-2333L).toShort()); this[1134] = ((-2413L).toShort()); this[1135] = ((-2477L).toShort()); this[1136] = ((-2542L).toShort()); this[1137] = ((-2574L).toShort()); this[1138] = ((-2607L).toShort()); this[1139] = ((-2622L).toShort()); this[1140] = ((-2655L).toShort()); this[1141] = ((1314).toShort()); this[1142] = ((1313).toShort()); this[1143] = ((1298).toShort()); this[1144] = ((1312).toShort()); this[1145] = ((1282).toShort()); this[1146] = ((785).toShort()); this[1147] = ((785).toShort()); this[1148] = ((785).toShort()); this[1149] = ((785).toShort()); this[1150] = ((1040).toShort()); this[1151] = ((1040).toShort()); this[1152] = ((1025).toShort()); this[1153] = ((1025).toShort()); this[1154] = ((768).toShort()); this[1155] = ((768).toShort()); this[1156] = ((768).toShort()); this[1157] = ((768).toShort()); this[1158] = ((-766L).toShort()); this[1159] = ((-798L).toShort()); this[1160] = ((-830L).toShort()); this[1161] = ((-862L).toShort()); this[1162] = ((-895L).toShort()); this[1163] = ((-911L).toShort()); this[1164] = ((-927L).toShort()); this[1165] = ((-943L).toShort()); this[1166] = ((-959L).toShort()); this[1167] = ((-975L).toShort()); this[1168] = ((-991L).toShort()); this[1169] = ((-1007L).toShort()); this[1170] = ((-1023L).toShort()); this[1171] = ((-1039L).toShort()); this[1172] = ((-1055L).toShort()); this[1173] = ((-1070L).toShort()); this[1174] = ((1724).toShort()); this[1175] = ((1647).toShort()); this[1176] = ((-1103L).toShort()); this[1177] = ((-1119L).toShort()); this[1178] = ((1631).toShort()); this[1179] = ((1767).toShort()); this[1180] = ((1662).toShort()); this[1181] = ((1738).toShort()); this[1182] = ((1708).toShort()); this[1183] = ((1723).toShort()); this[1184] = ((-1135L).toShort()); this[1185] = ((1780).toShort()); this[1186] = ((1615).toShort()); this[1187] = ((1779).toShort()); this[1188] = ((1599).toShort()); this[1189] = ((1677).toShort()); this[1190] = ((1646).toShort()); this[1191] = ((1778).toShort()); this[1192] = ((1583).toShort()); this[1193] = ((-1151L).toShort()); this[1194] = ((1777).toShort()); this[1195] = ((1567).toShort()); this[1196] = ((1737).toShort()); this[1197] = ((1692).toShort()); this[1198] = ((1765).toShort()); this[1199] = ((1722).toShort()); this[1200] = ((1707).toShort()); this[1201] = ((1630).toShort()); this[1202] = ((1751).toShort()); this[1203] = ((1661).toShort()); this[1204] = ((1764).toShort()); this[1205] = ((1614).toShort()); this[1206] = ((1736).toShort()); this[1207] = ((1676).toShort()); this[1208] = ((1763).toShort()); this[1209] = ((1750).toShort()); this[1210] = ((1645).toShort()); this[1211] = ((1598).toShort()); this[1212] = ((1721).toShort()); this[1213] = ((1691).toShort()); this[1214] = ((1762).toShort()); this[1215] = ((1706).toShort()); this[1216] = ((1582).toShort()); this[1217] = ((1761).toShort()); this[1218] = ((1566).toShort()); this[1219] = ((-1167L).toShort()); this[1220] = ((1749).toShort()); this[1221] = ((1629).toShort()); this[1222] = ((767).toShort()); this[1223] = ((766).toShort()); this[1224] = ((751).toShort()); this[1225] = ((765).toShort()); this[1226] = ((494).toShort()); this[1227] = ((494).toShort()); this[1228] = ((735).toShort()); this[1229] = ((764).toShort()); this[1230] = ((719).toShort()); this[1231] = ((749).toShort()); this[1232] = ((734).toShort()); this[1233] = ((763).toShort()); this[1234] = ((447).toShort()); this[1235] = ((447).toShort()); this[1236] = ((748).toShort()); this[1237] = ((718).toShort()); this[1238] = ((477).toShort()); this[1239] = ((506).toShort()); this[1240] = ((431).toShort()); this[1241] = ((491).toShort()); this[1242] = ((446).toShort()); this[1243] = ((476).toShort()); this[1244] = ((461).toShort()); this[1245] = ((505).toShort()); this[1246] = ((415).toShort()); this[1247] = ((430).toShort()); this[1248] = ((475).toShort()); this[1249] = ((445).toShort()); this[1250] = ((504).toShort()); this[1251] = ((399).toShort()); this[1252] = ((460).toShort()); this[1253] = ((489).toShort()); this[1254] = ((414).toShort()); this[1255] = ((503).toShort()); this[1256] = ((383).toShort()); this[1257] = ((474).toShort()); this[1258] = ((429).toShort()); this[1259] = ((459).toShort()); this[1260] = ((502).toShort()); this[1261] = ((502).toShort()); this[1262] = ((746).toShort()); this[1263] = ((752).toShort()); this[1264] = ((488).toShort()); this[1265] = ((398).toShort()); this[1266] = ((501).toShort()); this[1267] = ((473).toShort()); this[1268] = ((413).toShort()); this[1269] = ((472).toShort()); this[1270] = ((486).toShort()); this[1271] = ((271).toShort()); this[1272] = ((480).toShort()); this[1273] = ((270).toShort()); this[1274] = ((-1439L).toShort()); this[1275] = ((-1455L).toShort()); this[1276] = ((1357).toShort()); this[1277] = ((-1471L).toShort()); this[1278] = ((-1487L).toShort()); this[1279] = ((-1503L).toShort()); this[1280] = ((1341).toShort()); this[1281] = ((1325).toShort()); this[1282] = ((-1519L).toShort()); this[1283] = ((1489).toShort()); this[1284] = ((1463).toShort()); this[1285] = ((1403).toShort()); this[1286] = ((1309).toShort()); this[1287] = ((-1535L).toShort()); this[1288] = ((1372).toShort()); this[1289] = ((1448).toShort()); this[1290] = ((1418).toShort()); this[1291] = ((1476).toShort()); this[1292] = ((1356).toShort()); this[1293] = ((1462).toShort()); this[1294] = ((1387).toShort()); this[1295] = ((-1551L).toShort()); this[1296] = ((1475).toShort()); this[1297] = ((1340).toShort()); this[1298] = ((1447).toShort()); this[1299] = ((1402).toShort()); this[1300] = ((1386).toShort()); this[1301] = ((-1567L).toShort()); this[1302] = ((1068).toShort()); this[1303] = ((1068).toShort()); this[1304] = ((1474).toShort()); this[1305] = ((1461).toShort()); this[1306] = ((455).toShort()); this[1307] = ((380).toShort()); this[1308] = ((468).toShort()); this[1309] = ((440).toShort()); this[1310] = ((395).toShort()); this[1311] = ((425).toShort()); this[1312] = ((410).toShort()); this[1313] = ((454).toShort()); this[1314] = ((364).toShort()); this[1315] = ((467).toShort()); this[1316] = ((466).toShort()); this[1317] = ((464).toShort()); this[1318] = ((453).toShort()); this[1319] = ((269).toShort()); this[1320] = ((409).toShort()); this[1321] = ((448).toShort()); this[1322] = ((268).toShort()); this[1323] = ((432).toShort()); this[1324] = ((1371).toShort()); this[1325] = ((1473).toShort()); this[1326] = ((1432).toShort()); this[1327] = ((1417).toShort()); this[1328] = ((1308).toShort()); this[1329] = ((1460).toShort()); this[1330] = ((1355).toShort()); this[1331] = ((1446).toShort()); this[1332] = ((1459).toShort()); this[1333] = ((1431).toShort()); this[1334] = ((1083).toShort()); this[1335] = ((1083).toShort()); this[1336] = ((1401).toShort()); this[1337] = ((1416).toShort()); this[1338] = ((1458).toShort()); this[1339] = ((1445).toShort()); this[1340] = ((1067).toShort()); this[1341] = ((1067).toShort()); this[1342] = ((1370).toShort()); this[1343] = ((1457).toShort()); this[1344] = ((1051).toShort()); this[1345] = ((1051).toShort()); this[1346] = ((1291).toShort()); this[1347] = ((1430).toShort()); this[1348] = ((1385).toShort()); this[1349] = ((1444).toShort()); this[1350] = ((1354).toShort()); this[1351] = ((1415).toShort()); this[1352] = ((1400).toShort()); this[1353] = ((1443).toShort()); this[1354] = ((1082).toShort()); this[1355] = ((1082).toShort()); this[1356] = ((1173).toShort()); this[1357] = ((1113).toShort()); this[1358] = ((1186).toShort()); this[1359] = ((1066).toShort()); this[1360] = ((1185).toShort()); this[1361] = ((1050).toShort()); this[1362] = ((-1967L).toShort()); this[1363] = ((1158).toShort()); this[1364] = ((1128).toShort()); this[1365] = ((1172).toShort()); this[1366] = ((1097).toShort()); this[1367] = ((1171).toShort()); this[1368] = ((1081).toShort()); this[1369] = ((-1983L).toShort()); this[1370] = ((1157).toShort()); this[1371] = ((1112).toShort()); this[1372] = ((416).toShort()); this[1373] = ((266).toShort()); this[1374] = ((375).toShort()); this[1375] = ((400).toShort()); this[1376] = ((1170).toShort()); this[1377] = ((1142).toShort()); this[1378] = ((1127).toShort()); this[1379] = ((1065).toShort()); this[1380] = ((793).toShort()); this[1381] = ((793).toShort()); this[1382] = ((1169).toShort()); this[1383] = ((1033).toShort()); this[1384] = ((1156).toShort()); this[1385] = ((1096).toShort()); this[1386] = ((1141).toShort()); this[1387] = ((1111).toShort()); this[1388] = ((1155).toShort()); this[1389] = ((1080).toShort()); this[1390] = ((1126).toShort()); this[1391] = ((1140).toShort()); this[1392] = ((898).toShort()); this[1393] = ((898).toShort()); this[1394] = ((808).toShort()); this[1395] = ((808).toShort()); this[1396] = ((897).toShort()); this[1397] = ((897).toShort()); this[1398] = ((792).toShort()); this[1399] = ((792).toShort()); this[1400] = ((1095).toShort()); this[1401] = ((1152).toShort()); this[1402] = ((1032).toShort()); this[1403] = ((1125).toShort()); this[1404] = ((1110).toShort()); this[1405] = ((1139).toShort()); this[1406] = ((1079).toShort()); this[1407] = ((1124).toShort()); this[1408] = ((882).toShort()); this[1409] = ((807).toShort()); this[1410] = ((838).toShort()); this[1411] = ((881).toShort()); this[1412] = ((853).toShort()); this[1413] = ((791).toShort()); this[1414] = ((-2319L).toShort()); this[1415] = ((867).toShort()); this[1416] = ((368).toShort()); this[1417] = ((263).toShort()); this[1418] = ((822).toShort()); this[1419] = ((852).toShort()); this[1420] = ((837).toShort()); this[1421] = ((866).toShort()); this[1422] = ((806).toShort()); this[1423] = ((865).toShort()); this[1424] = ((-2399L).toShort()); this[1425] = ((851).toShort()); this[1426] = ((352).toShort()); this[1427] = ((262).toShort()); this[1428] = ((534).toShort()); this[1429] = ((534).toShort()); this[1430] = ((821).toShort()); this[1431] = ((836).toShort()); this[1432] = ((594).toShort()); this[1433] = ((594).toShort()); this[1434] = ((549).toShort()); this[1435] = ((549).toShort()); this[1436] = ((593).toShort()); this[1437] = ((593).toShort()); this[1438] = ((533).toShort()); this[1439] = ((533).toShort()); this[1440] = ((848).toShort()); this[1441] = ((773).toShort()); this[1442] = ((579).toShort()); this[1443] = ((579).toShort()); this[1444] = ((564).toShort()); this[1445] = ((578).toShort()); this[1446] = ((548).toShort()); this[1447] = ((563).toShort()); this[1448] = ((276).toShort()); this[1449] = ((276).toShort()); this[1450] = ((577).toShort()); this[1451] = ((576).toShort()); this[1452] = ((306).toShort()); this[1453] = ((291).toShort()); this[1454] = ((516).toShort()); this[1455] = ((560).toShort()); this[1456] = ((305).toShort()); this[1457] = ((305).toShort()); this[1458] = ((275).toShort()); this[1459] = ((259).toShort()); this[1460] = ((-251L).toShort()); this[1461] = ((-892L).toShort()); this[1462] = ((-2058L).toShort()); this[1463] = ((-2620L).toShort()); this[1464] = ((-2828L).toShort()); this[1465] = ((-2957L).toShort()); this[1466] = ((-3023L).toShort()); this[1467] = ((-3039L).toShort()); this[1468] = ((1041).toShort()); this[1469] = ((1041).toShort()); this[1470] = ((1040).toShort()); this[1471] = ((1040).toShort()); this[1472] = ((769).toShort()); this[1473] = ((769).toShort()); this[1474] = ((769).toShort()); this[1475] = ((769).toShort()); this[1476] = ((256).toShort()); this[1477] = ((256).toShort()); this[1478] = ((256).toShort()); this[1479] = ((256).toShort()); this[1480] = ((256).toShort()); this[1481] = ((256).toShort()); this[1482] = ((256).toShort()); this[1483] = ((256).toShort()); this[1484] = ((256).toShort()); this[1485] = ((256).toShort()); this[1486] = ((256).toShort()); this[1487] = ((256).toShort()); this[1488] = ((256).toShort()); this[1489] = ((256).toShort()); this[1490] = ((256).toShort()); this[1491] = ((256).toShort()); this[1492] = ((-511L).toShort()); this[1493] = ((-527L).toShort()); this[1494] = ((-543L).toShort()); this[1495] = ((-559L).toShort()); this[1496] = ((1530).toShort()); this[1497] = ((-575L).toShort()); this[1498] = ((-591L).toShort()); this[1499] = ((1528).toShort()); this[1500] = ((1527).toShort()); this[1501] = ((1407).toShort()); this[1502] = ((1526).toShort()); this[1503] = ((1391).toShort()); this[1504] = ((1023).toShort()); this[1505] = ((1023).toShort()); this[1506] = ((1023).toShort()); this[1507] = ((1023).toShort()); this[1508] = ((1525).toShort()); this[1509] = ((1375).toShort()); this[1510] = ((1268).toShort()); this[1511] = ((1268).toShort()); this[1512] = ((1103).toShort()); this[1513] = ((1103).toShort()); this[1514] = ((1087).toShort()); this[1515] = ((1087).toShort()); this[1516] = ((1039).toShort()); this[1517] = ((1039).toShort()); this[1518] = ((1523).toShort()); this[1519] = ((-604L).toShort()); this[1520] = ((815).toShort()); this[1521] = ((815).toShort()); this[1522] = ((815).toShort()); this[1523] = ((815).toShort()); this[1524] = ((510).toShort()); this[1525] = ((495).toShort()); this[1526] = ((509).toShort()); this[1527] = ((479).toShort()); this[1528] = ((508).toShort()); this[1529] = ((463).toShort()); this[1530] = ((507).toShort()); this[1531] = ((447).toShort()); this[1532] = ((431).toShort()); this[1533] = ((505).toShort()); this[1534] = ((415).toShort()); this[1535] = ((399).toShort()); this[1536] = ((-734L).toShort()); this[1537] = ((-782L).toShort()); this[1538] = ((1262).toShort()); this[1539] = ((-815L).toShort()); this[1540] = ((1259).toShort()); this[1541] = ((1244).toShort()); this[1542] = ((-831L).toShort()); this[1543] = ((1258).toShort()); this[1544] = ((1228).toShort()); this[1545] = ((-847L).toShort()); this[1546] = ((-863L).toShort()); this[1547] = ((1196).toShort()); this[1548] = ((-879L).toShort()); this[1549] = ((1253).toShort()); this[1550] = ((987).toShort()); this[1551] = ((987).toShort()); this[1552] = ((748).toShort()); this[1553] = ((-767L).toShort()); this[1554] = ((493).toShort()); this[1555] = ((493).toShort()); this[1556] = ((462).toShort()); this[1557] = ((477).toShort()); this[1558] = ((414).toShort()); this[1559] = ((414).toShort()); this[1560] = ((686).toShort()); this[1561] = ((669).toShort()); this[1562] = ((478).toShort()); this[1563] = ((446).toShort()); this[1564] = ((461).toShort()); this[1565] = ((445).toShort()); this[1566] = ((474).toShort()); this[1567] = ((429).toShort()); this[1568] = ((487).toShort()); this[1569] = ((458).toShort()); this[1570] = ((412).toShort()); this[1571] = ((471).toShort()); this[1572] = ((1266).toShort()); this[1573] = ((1264).toShort()); this[1574] = ((1009).toShort()); this[1575] = ((1009).toShort()); this[1576] = ((799).toShort()); this[1577] = ((799).toShort()); this[1578] = ((-1019L).toShort()); this[1579] = ((-1276L).toShort()); this[1580] = ((-1452L).toShort()); this[1581] = ((-1581L).toShort()); this[1582] = ((-1677L).toShort()); this[1583] = ((-1757L).toShort()); this[1584] = ((-1821L).toShort()); this[1585] = ((-1886L).toShort()); this[1586] = ((-1933L).toShort()); this[1587] = ((-1997L).toShort()); this[1588] = ((1257).toShort()); this[1589] = ((1257).toShort()); this[1590] = ((1483).toShort()); this[1591] = ((1468).toShort()); this[1592] = ((1512).toShort()); this[1593] = ((1422).toShort()); this[1594] = ((1497).toShort()); this[1595] = ((1406).toShort()); this[1596] = ((1467).toShort()); this[1597] = ((1496).toShort()); this[1598] = ((1421).toShort()); this[1599] = ((1510).toShort()); this[1600] = ((1134).toShort()); this[1601] = ((1134).toShort()); this[1602] = ((1225).toShort()); this[1603] = ((1225).toShort()); this[1604] = ((1466).toShort()); this[1605] = ((1451).toShort()); this[1606] = ((1374).toShort()); this[1607] = ((1405).toShort()); this[1608] = ((1252).toShort()); this[1609] = ((1252).toShort()); this[1610] = ((1358).toShort()); this[1611] = ((1480).toShort()); this[1612] = ((1164).toShort()); this[1613] = ((1164).toShort()); this[1614] = ((1251).toShort()); this[1615] = ((1251).toShort()); this[1616] = ((1238).toShort()); this[1617] = ((1238).toShort()); this[1618] = ((1389).toShort()); this[1619] = ((1465).toShort()); this[1620] = ((-1407L).toShort()); this[1621] = ((1054).toShort()); this[1622] = ((1101).toShort()); this[1623] = ((-1423L).toShort()); this[1624] = ((1207).toShort()); this[1625] = ((-1439L).toShort()); this[1626] = ((830).toShort()); this[1627] = ((830).toShort()); this[1628] = ((1248).toShort()); this[1629] = ((1038).toShort()); this[1630] = ((1237).toShort()); this[1631] = ((1117).toShort()); this[1632] = ((1223).toShort()); this[1633] = ((1148).toShort()); this[1634] = ((1236).toShort()); this[1635] = ((1208).toShort()); this[1636] = ((411).toShort()); this[1637] = ((426).toShort()); this[1638] = ((395).toShort()); this[1639] = ((410).toShort()); this[1640] = ((379).toShort()); this[1641] = ((269).toShort()); this[1642] = ((1193).toShort()); this[1643] = ((1222).toShort()); this[1644] = ((1132).toShort()); this[1645] = ((1235).toShort()); this[1646] = ((1221).toShort()); this[1647] = ((1116).toShort()); this[1648] = ((976).toShort()); this[1649] = ((976).toShort()); this[1650] = ((1192).toShort()); this[1651] = ((1162).toShort()); this[1652] = ((1177).toShort()); this[1653] = ((1220).toShort()); this[1654] = ((1131).toShort()); this[1655] = ((1191).toShort()); this[1656] = ((963).toShort()); this[1657] = ((963).toShort()); this[1658] = ((-1647L).toShort()); this[1659] = ((961).toShort()); this[1660] = ((780).toShort()); this[1661] = ((-1663L).toShort()); this[1662] = ((558).toShort()); this[1663] = ((558).toShort()); this[1664] = ((994).toShort()); this[1665] = ((993).toShort()); this[1666] = ((437).toShort()); this[1667] = ((408).toShort()); this[1668] = ((393).toShort()); this[1669] = ((407).toShort()); this[1670] = ((829).toShort()); this[1671] = ((978).toShort()); this[1672] = ((813).toShort()); this[1673] = ((797).toShort()); this[1674] = ((947).toShort()); this[1675] = ((-1743L).toShort()); this[1676] = ((721).toShort()); this[1677] = ((721).toShort()); this[1678] = ((377).toShort()); this[1679] = ((392).toShort()); this[1680] = ((844).toShort()); this[1681] = ((950).toShort()); this[1682] = ((828).toShort()); this[1683] = ((890).toShort()); this[1684] = ((706).toShort()); this[1685] = ((706).toShort()); this[1686] = ((812).toShort()); this[1687] = ((859).toShort()); this[1688] = ((796).toShort()); this[1689] = ((960).toShort()); this[1690] = ((948).toShort()); this[1691] = ((843).toShort()); this[1692] = ((934).toShort()); this[1693] = ((874).toShort()); this[1694] = ((571).toShort()); this[1695] = ((571).toShort()); this[1696] = ((-1919L).toShort()); this[1697] = ((690).toShort()); this[1698] = ((555).toShort()); this[1699] = ((689).toShort()); this[1700] = ((421).toShort()); this[1701] = ((346).toShort()); this[1702] = ((539).toShort()); this[1703] = ((539).toShort()); this[1704] = ((944).toShort()); this[1705] = ((779).toShort()); this[1706] = ((918).toShort()); this[1707] = ((873).toShort()); this[1708] = ((932).toShort()); this[1709] = ((842).toShort()); this[1710] = ((903).toShort()); this[1711] = ((888).toShort()); this[1712] = ((570).toShort()); this[1713] = ((570).toShort()); this[1714] = ((931).toShort()); this[1715] = ((917).toShort()); this[1716] = ((674).toShort()); this[1717] = ((674).toShort()); this[1718] = ((-2575L).toShort()); this[1719] = ((1562).toShort()); this[1720] = ((-2591L).toShort()); this[1721] = ((1609).toShort()); this[1722] = ((-2607L).toShort()); this[1723] = ((1654).toShort()); this[1724] = ((1322).toShort()); this[1725] = ((1322).toShort()); this[1726] = ((1441).toShort()); this[1727] = ((1441).toShort()); this[1728] = ((1696).toShort()); this[1729] = ((1546).toShort()); this[1730] = ((1683).toShort()); this[1731] = ((1593).toShort()); this[1732] = ((1669).toShort()); this[1733] = ((1624).toShort()); this[1734] = ((1426).toShort()); this[1735] = ((1426).toShort()); this[1736] = ((1321).toShort()); this[1737] = ((1321).toShort()); this[1738] = ((1639).toShort()); this[1739] = ((1680).toShort()); this[1740] = ((1425).toShort()); this[1741] = ((1425).toShort()); this[1742] = ((1305).toShort()); this[1743] = ((1305).toShort()); this[1744] = ((1545).toShort()); this[1745] = ((1668).toShort()); this[1746] = ((1608).toShort()); this[1747] = ((1623).toShort()); this[1748] = ((1667).toShort()); this[1749] = ((1592).toShort()); this[1750] = ((1638).toShort()); this[1751] = ((1666).toShort()); this[1752] = ((1320).toShort()); this[1753] = ((1320).toShort()); this[1754] = ((1652).toShort()); this[1755] = ((1607).toShort()); this[1756] = ((1409).toShort()); this[1757] = ((1409).toShort()); this[1758] = ((1304).toShort()); this[1759] = ((1304).toShort()); this[1760] = ((1288).toShort()); this[1761] = ((1288).toShort()); this[1762] = ((1664).toShort()); this[1763] = ((1637).toShort()); this[1764] = ((1395).toShort()); this[1765] = ((1395).toShort()); this[1766] = ((1335).toShort()); this[1767] = ((1335).toShort()); this[1768] = ((1622).toShort()); this[1769] = ((1636).toShort()); this[1770] = ((1394).toShort()); this[1771] = ((1394).toShort()); this[1772] = ((1319).toShort()); this[1773] = ((1319).toShort()); this[1774] = ((1606).toShort()); this[1775] = ((1621).toShort()); this[1776] = ((1392).toShort()); this[1777] = ((1392).toShort()); this[1778] = ((1137).toShort()); this[1779] = ((1137).toShort()); this[1780] = ((1137).toShort()); this[1781] = ((1137).toShort()); this[1782] = ((345).toShort()); this[1783] = ((390).toShort()); this[1784] = ((360).toShort()); this[1785] = ((375).toShort()); this[1786] = ((404).toShort()); this[1787] = ((373).toShort()); this[1788] = ((1047).toShort()); this[1789] = ((-2751L).toShort()); this[1790] = ((-2767L).toShort()); this[1791] = ((-2783L).toShort()); this[1792] = ((1062).toShort()); this[1793] = ((1121).toShort()); this[1794] = ((1046).toShort()); this[1795] = ((-2799L).toShort()); this[1796] = ((1077).toShort()); this[1797] = ((-2815L).toShort()); this[1798] = ((1106).toShort()); this[1799] = ((1061).toShort()); this[1800] = ((789).toShort()); this[1801] = ((789).toShort()); this[1802] = ((1105).toShort()); this[1803] = ((1104).toShort()); this[1804] = ((263).toShort()); this[1805] = ((355).toShort()); this[1806] = ((310).toShort()); this[1807] = ((340).toShort()); this[1808] = ((325).toShort()); this[1809] = ((354).toShort()); this[1810] = ((352).toShort()); this[1811] = ((262).toShort()); this[1812] = ((339).toShort()); this[1813] = ((324).toShort()); this[1814] = ((1091).toShort()); this[1815] = ((1076).toShort()); this[1816] = ((1029).toShort()); this[1817] = ((1090).toShort()); this[1818] = ((1060).toShort()); this[1819] = ((1075).toShort()); this[1820] = ((833).toShort()); this[1821] = ((833).toShort()); this[1822] = ((788).toShort()); this[1823] = ((788).toShort()); this[1824] = ((1088).toShort()); this[1825] = ((1028).toShort()); this[1826] = ((818).toShort()); this[1827] = ((818).toShort()); this[1828] = ((803).toShort()); this[1829] = ((803).toShort()); this[1830] = ((561).toShort()); this[1831] = ((561).toShort()); this[1832] = ((531).toShort()); this[1833] = ((531).toShort()); this[1834] = ((816).toShort()); this[1835] = ((771).toShort()); this[1836] = ((546).toShort()); this[1837] = ((546).toShort()); this[1838] = ((289).toShort()); this[1839] = ((274).toShort()); this[1840] = ((288).toShort()); this[1841] = ((258).toShort()); this[1842] = ((-253L).toShort()); this[1843] = ((-317L).toShort()); this[1844] = ((-381L).toShort()); this[1845] = ((-446L).toShort()); this[1846] = ((-478L).toShort()); this[1847] = ((-509L).toShort()); this[1848] = ((1279).toShort()); this[1849] = ((1279).toShort()); this[1850] = ((-811L).toShort()); this[1851] = ((-1179L).toShort()); this[1852] = ((-1451L).toShort()); this[1853] = ((-1756L).toShort()); this[1854] = ((-1900L).toShort()); this[1855] = ((-2028L).toShort()); this[1856] = ((-2189L).toShort()); this[1857] = ((-2253L).toShort()); this[1858] = ((-2333L).toShort()); this[1859] = ((-2414L).toShort()); this[1860] = ((-2445L).toShort()); this[1861] = ((-2511L).toShort()); this[1862] = ((-2526L).toShort()); this[1863] = ((1313).toShort()); this[1864] = ((1298).toShort()); this[1865] = ((-2559L).toShort()); this[1866] = ((1041).toShort()); this[1867] = ((1041).toShort()); this[1868] = ((1040).toShort()); this[1869] = ((1040).toShort()); this[1870] = ((1025).toShort()); this[1871] = ((1025).toShort()); this[1872] = ((1024).toShort()); this[1873] = ((1024).toShort()); this[1874] = ((1022).toShort()); this[1875] = ((1007).toShort()); this[1876] = ((1021).toShort()); this[1877] = ((991).toShort()); this[1878] = ((1020).toShort()); this[1879] = ((975).toShort()); this[1880] = ((1019).toShort()); this[1881] = ((959).toShort()); this[1882] = ((687).toShort()); this[1883] = ((687).toShort()); this[1884] = ((1018).toShort()); this[1885] = ((1017).toShort()); this[1886] = ((671).toShort()); this[1887] = ((671).toShort()); this[1888] = ((655).toShort()); this[1889] = ((655).toShort()); this[1890] = ((1016).toShort()); this[1891] = ((1015).toShort()); this[1892] = ((639).toShort()); this[1893] = ((639).toShort()); this[1894] = ((758).toShort()); this[1895] = ((758).toShort()); this[1896] = ((623).toShort()); this[1897] = ((623).toShort()); this[1898] = ((757).toShort()); this[1899] = ((607).toShort()); this[1900] = ((756).toShort()); this[1901] = ((591).toShort()); this[1902] = ((755).toShort()); this[1903] = ((575).toShort()); this[1904] = ((754).toShort()); this[1905] = ((559).toShort()); this[1906] = ((543).toShort()); this[1907] = ((543).toShort()); this[1908] = ((1009).toShort()); this[1909] = ((783).toShort()); this[1910] = ((-575L).toShort()); this[1911] = ((-621L).toShort()); this[1912] = ((-685L).toShort()); this[1913] = ((-749L).toShort()); this[1914] = ((496).toShort()); this[1915] = ((-590L).toShort()); this[1916] = ((750).toShort()); this[1917] = ((749).toShort()); this[1918] = ((734).toShort()); this[1919] = ((748).toShort()); this[1920] = ((974).toShort()); this[1921] = ((989).toShort()); this[1922] = ((1003).toShort()); this[1923] = ((958).toShort()); this[1924] = ((988).toShort()); this[1925] = ((973).toShort()); this[1926] = ((1002).toShort()); this[1927] = ((942).toShort()); this[1928] = ((987).toShort()); this[1929] = ((957).toShort()); this[1930] = ((972).toShort()); this[1931] = ((1001).toShort()); this[1932] = ((926).toShort()); this[1933] = ((986).toShort()); this[1934] = ((941).toShort()); this[1935] = ((971).toShort()); this[1936] = ((956).toShort()); this[1937] = ((1000).toShort()); this[1938] = ((910).toShort()); this[1939] = ((985).toShort()); this[1940] = ((925).toShort()); this[1941] = ((999).toShort()); this[1942] = ((894).toShort()); this[1943] = ((970).toShort()); this[1944] = ((-1071L).toShort()); this[1945] = ((-1087L).toShort()); this[1946] = ((-1102L).toShort()); this[1947] = ((1390).toShort()); this[1948] = ((-1135L).toShort()); this[1949] = ((1436).toShort()); this[1950] = ((1509).toShort()); this[1951] = ((1451).toShort()); this[1952] = ((1374).toShort()); this[1953] = ((-1151L).toShort()); this[1954] = ((1405).toShort()); this[1955] = ((1358).toShort()); this[1956] = ((1480).toShort()); this[1957] = ((1420).toShort()); this[1958] = ((-1167L).toShort()); this[1959] = ((1507).toShort()); this[1960] = ((1494).toShort()); this[1961] = ((1389).toShort()); this[1962] = ((1342).toShort()); this[1963] = ((1465).toShort()); this[1964] = ((1435).toShort()); this[1965] = ((1450).toShort()); this[1966] = ((1326).toShort()); this[1967] = ((1505).toShort()); this[1968] = ((1310).toShort()); this[1969] = ((1493).toShort()); this[1970] = ((1373).toShort()); this[1971] = ((1479).toShort()); this[1972] = ((1404).toShort()); this[1973] = ((1492).toShort()); this[1974] = ((1464).toShort()); this[1975] = ((1419).toShort()); this[1976] = ((428).toShort()); this[1977] = ((443).toShort()); this[1978] = ((472).toShort()); this[1979] = ((397).toShort()); this[1980] = ((736).toShort()); this[1981] = ((526).toShort()); this[1982] = ((464).toShort()); this[1983] = ((464).toShort()); this[1984] = ((486).toShort()); this[1985] = ((457).toShort()); this[1986] = ((442).toShort()); this[1987] = ((471).toShort()); this[1988] = ((484).toShort()); this[1989] = ((482).toShort()); this[1990] = ((1357).toShort()); this[1991] = ((1449).toShort()); this[1992] = ((1434).toShort()); this[1993] = ((1478).toShort()); this[1994] = ((1388).toShort()); this[1995] = ((1491).toShort()); this[1996] = ((1341).toShort()); this[1997] = ((1490).toShort()); this[1998] = ((1325).toShort()); this[1999] = ((1489).toShort()); this[2000] = ((1463).toShort()); this[2001] = ((1403).toShort()); this[2002] = ((1309).toShort()); this[2003] = ((1477).toShort()); this[2004] = ((1372).toShort()); this[2005] = ((1448).toShort()); this[2006] = ((1418).toShort()); this[2007] = ((1433).toShort()); this[2008] = ((1476).toShort()); this[2009] = ((1356).toShort()); this[2010] = ((1462).toShort()); this[2011] = ((1387).toShort()); this[2012] = ((-1439L).toShort()); this[2013] = ((1475).toShort()); this[2014] = ((1340).toShort()); this[2015] = ((1447).toShort()); this[2016] = ((1402).toShort()); this[2017] = ((1474).toShort()); this[2018] = ((1324).toShort()); this[2019] = ((1461).toShort()); this[2020] = ((1371).toShort()); this[2021] = ((1473).toShort()); this[2022] = ((269).toShort()); this[2023] = ((448).toShort()); this[2024] = ((1432).toShort()); this[2025] = ((1417).toShort()); this[2026] = ((1308).toShort()); this[2027] = ((1460).toShort()); this[2028] = ((-1711L).toShort()); this[2029] = ((1459).toShort()); this[2030] = ((-1727L).toShort()); this[2031] = ((1441).toShort()); this[2032] = ((1099).toShort()); this[2033] = ((1099).toShort()); this[2034] = ((1446).toShort()); this[2035] = ((1386).toShort()); this[2036] = ((1431).toShort()); this[2037] = ((1401).toShort()); this[2038] = ((-1743L).toShort()); this[2039] = ((1289).toShort()); this[2040] = ((1083).toShort()); this[2041] = ((1083).toShort()); this[2042] = ((1160).toShort()); this[2043] = ((1160).toShort()); this[2044] = ((1458).toShort()); this[2045] = ((1445).toShort()); this[2046] = ((1067).toShort()); this[2047] = ((1067).toShort()); this[2048] = ((1370).toShort()); this[2049] = ((1457).toShort()); this[2050] = ((1307).toShort()); this[2051] = ((1430).toShort()); this[2052] = ((1129).toShort()); this[2053] = ((1129).toShort()); this[2054] = ((1098).toShort()); this[2055] = ((1098).toShort()); this[2056] = ((268).toShort()); this[2057] = ((432).toShort()); this[2058] = ((267).toShort()); this[2059] = ((416).toShort()); this[2060] = ((266).toShort()); this[2061] = ((400).toShort()); this[2062] = ((-1887L).toShort()); this[2063] = ((1144).toShort()); this[2064] = ((1187).toShort()); this[2065] = ((1082).toShort()); this[2066] = ((1173).toShort()); this[2067] = ((1113).toShort()); this[2068] = ((1186).toShort()); this[2069] = ((1066).toShort()); this[2070] = ((1050).toShort()); this[2071] = ((1158).toShort()); this[2072] = ((1128).toShort()); this[2073] = ((1143).toShort()); this[2074] = ((1172).toShort()); this[2075] = ((1097).toShort()); this[2076] = ((1171).toShort()); this[2077] = ((1081).toShort()); this[2078] = ((420).toShort()); this[2079] = ((391).toShort()); this[2080] = ((1157).toShort()); this[2081] = ((1112).toShort()); this[2082] = ((1170).toShort()); this[2083] = ((1142).toShort()); this[2084] = ((1127).toShort()); this[2085] = ((1065).toShort()); this[2086] = ((1169).toShort()); this[2087] = ((1049).toShort()); this[2088] = ((1156).toShort()); this[2089] = ((1096).toShort()); this[2090] = ((1141).toShort()); this[2091] = ((1111).toShort()); this[2092] = ((1155).toShort()); this[2093] = ((1080).toShort()); this[2094] = ((1126).toShort()); this[2095] = ((1154).toShort()); this[2096] = ((1064).toShort()); this[2097] = ((1153).toShort()); this[2098] = ((1140).toShort()); this[2099] = ((1095).toShort()); this[2100] = ((1048).toShort()); this[2101] = ((-2159L).toShort()); this[2102] = ((1125).toShort()); this[2103] = ((1110).toShort()); this[2104] = ((1137).toShort()); this[2105] = ((-2175L).toShort()); this[2106] = ((823).toShort()); this[2107] = ((823).toShort()); this[2108] = ((1139).toShort()); this[2109] = ((1138).toShort()); this[2110] = ((807).toShort()); this[2111] = ((807).toShort()); this[2112] = ((384).toShort()); this[2113] = ((264).toShort()); this[2114] = ((368).toShort()); this[2115] = ((263).toShort()); this[2116] = ((868).toShort()); this[2117] = ((838).toShort()); this[2118] = ((853).toShort()); this[2119] = ((791).toShort()); this[2120] = ((867).toShort()); this[2121] = ((822).toShort()); this[2122] = ((852).toShort()); this[2123] = ((837).toShort()); this[2124] = ((866).toShort()); this[2125] = ((806).toShort()); this[2126] = ((865).toShort()); this[2127] = ((790).toShort()); this[2128] = ((-2319L).toShort()); this[2129] = ((851).toShort()); this[2130] = ((821).toShort()); this[2131] = ((836).toShort()); this[2132] = ((352).toShort()); this[2133] = ((262).toShort()); this[2134] = ((850).toShort()); this[2135] = ((805).toShort()); this[2136] = ((849).toShort()); this[2137] = ((-2399L).toShort()); this[2138] = ((533).toShort()); this[2139] = ((533).toShort()); this[2140] = ((835).toShort()); this[2141] = ((820).toShort()); this[2142] = ((336).toShort()); this[2143] = ((261).toShort()); this[2144] = ((578).toShort()); this[2145] = ((548).toShort()); this[2146] = ((563).toShort()); this[2147] = ((577).toShort()); this[2148] = ((532).toShort()); this[2149] = ((532).toShort()); this[2150] = ((832).toShort()); this[2151] = ((772).toShort()); this[2152] = ((562).toShort()); this[2153] = ((562).toShort()); this[2154] = ((547).toShort()); this[2155] = ((547).toShort()); this[2156] = ((305).toShort()); this[2157] = ((275).toShort()); this[2158] = ((560).toShort()); this[2159] = ((515).toShort()); this[2160] = ((290).toShort()); this[2161] = ((290).toShort()); this[2162] = ((288).toShort()); this[2163] = ((258).toShort()) } }()
    private var __STATIC_L3_huffman_tab32: CPointer<UByte> = { fixedArrayOfUByte(28) { this[0] = ((130).toUByte()); this[1] = ((162).toUByte()); this[2] = ((193).toUByte()); this[3] = ((209).toUByte()); this[4] = ((44).toUByte()); this[5] = ((28).toUByte()); this[6] = ((76).toUByte()); this[7] = ((140).toUByte()); this[8] = ((9).toUByte()); this[9] = ((9).toUByte()); this[10] = ((9).toUByte()); this[11] = ((9).toUByte()); this[12] = ((9).toUByte()); this[13] = ((9).toUByte()); this[14] = ((9).toUByte()); this[15] = ((9).toUByte()); this[16] = ((190).toUByte()); this[17] = ((254).toUByte()); this[18] = ((222).toUByte()); this[19] = ((238).toUByte()); this[20] = ((126).toUByte()); this[21] = ((94).toUByte()); this[22] = ((157).toUByte()); this[23] = ((157).toUByte()); this[24] = ((109).toUByte()); this[25] = ((61).toUByte()); this[26] = ((173).toUByte()); this[27] = ((205).toUByte()) } }()
    private var __STATIC_L3_huffman_tab33: CPointer<UByte> = { fixedArrayOfUByte(16) { this[0] = ((252).toUByte()); this[1] = ((236).toUByte()); this[2] = ((220).toUByte()); this[3] = ((204).toUByte()); this[4] = ((188).toUByte()); this[5] = ((172).toUByte()); this[6] = ((156).toUByte()); this[7] = ((140).toUByte()); this[8] = ((124).toUByte()); this[9] = ((108).toUByte()); this[10] = ((92).toUByte()); this[11] = ((76).toUByte()); this[12] = ((60).toUByte()); this[13] = ((44).toUByte()); this[14] = ((28).toUByte()); this[15] = ((12).toUByte()) } }()
    private var __STATIC_L3_huffman_tabindex: Array32Short = { Array32ShortAlloc { this[0] = ((0).toShort()); this[1] = ((32).toShort()); this[2] = ((64).toShort()); this[3] = ((98).toShort()); this[4] = ((0).toShort()); this[5] = ((132).toShort()); this[6] = ((180).toShort()); this[7] = ((218).toShort()); this[8] = ((292).toShort()); this[9] = ((364).toShort()); this[10] = ((426).toShort()); this[11] = ((538).toShort()); this[12] = ((648).toShort()); this[13] = ((746).toShort()); this[14] = ((0).toShort()); this[15] = ((1126).toShort()); this[16] = ((1460).toShort()); this[17] = ((1460).toShort()); this[18] = ((1460).toShort()); this[19] = ((1460).toShort()); this[20] = ((1460).toShort()); this[21] = ((1460).toShort()); this[22] = ((1460).toShort()); this[23] = ((1460).toShort()); this[24] = ((1842).toShort()); this[25] = ((1842).toShort()); this[26] = ((1842).toShort()); this[27] = ((1842).toShort()); this[28] = ((1842).toShort()); this[29] = ((1842).toShort()); this[30] = ((1842).toShort()); this[31] = ((1842).toShort()) } }()
    private var __STATIC_L3_huffman_g_linbits: CPointer<UByte> = { fixedArrayOfUByte(32) { this[0] = ((0).toUByte()); this[1] = ((0).toUByte()); this[2] = ((0).toUByte()); this[3] = ((0).toUByte()); this[4] = ((0).toUByte()); this[5] = ((0).toUByte()); this[6] = ((0).toUByte()); this[7] = ((0).toUByte()); this[8] = ((0).toUByte()); this[9] = ((0).toUByte()); this[10] = ((0).toUByte()); this[11] = ((0).toUByte()); this[12] = ((0).toUByte()); this[13] = ((0).toUByte()); this[14] = ((0).toUByte()); this[15] = ((0).toUByte()); this[16] = ((1).toUByte()); this[17] = ((2).toUByte()); this[18] = ((3).toUByte()); this[19] = ((4).toUByte()); this[20] = ((6).toUByte()); this[21] = ((8).toUByte()); this[22] = ((10).toUByte()); this[23] = ((13).toUByte()); this[24] = ((4).toUByte()); this[25] = ((5).toUByte()); this[26] = ((6).toUByte()); this[27] = ((7).toUByte()); this[28] = ((8).toUByte()); this[29] = ((9).toUByte()); this[30] = ((11).toUByte()); this[31] = ((13).toUByte()) } }()
    private var __STATIC_L3_stereo_process_g_pan: Array14Float = { Array14FloatAlloc { this[0] = 0f; this[1] = 1f; this[2] = 0.21132487f; this[3] = 0.78867513f; this[4] = 0.3660254f; this[5] = 0.6339746f; this[6] = 0.5f; this[7] = 0.5f; this[8] = 0.6339746f; this[9] = 0.3660254f; this[10] = 0.78867513f; this[11] = 0.21132487f; this[12] = 1f; this[13] = 0f } }()
    private var __STATIC_L3_antialias_g_aa: Array2Array8Float = { Array2Array8FloatAlloc { this[0] = Array8FloatAlloc { this[0] = 0.85749293f; this[1] = 0.881742f; this[2] = 0.94962865f; this[3] = 0.98331459f; this[4] = 0.99551782f; this[5] = 0.99916056f; this[6] = 0.9998992f; this[7] = 0.99999316f }; this[1] = Array8FloatAlloc { this[0] = 0.51449576f; this[1] = 0.47173197f; this[2] = 0.31337745f; this[3] = 0.1819132f; this[4] = 0.09457419f; this[5] = 0.04096558f; this[6] = 0.01419856f; this[7] = 0.00369997f } } }()
    private var __STATIC_L3_imdct36_g_twid9: Array18Float = { Array18FloatAlloc { this[0] = 0.73727734f; this[1] = 0.79335334f; this[2] = 0.84339145f; this[3] = 0.88701083f; this[4] = 0.92387953f; this[5] = 0.95371695f; this[6] = 0.97629601f; this[7] = 0.99144486f; this[8] = 0.99904822f; this[9] = 0.67559021f; this[10] = 0.60876143f; this[11] = 0.53729961f; this[12] = 0.46174861f; this[13] = 0.38268343f; this[14] = 0.3007058f; this[15] = 0.21643961f; this[16] = 0.13052619f; this[17] = 0.04361938f } }()
    private var __STATIC_L3_imdct12_g_twid3: Array6Float = { Array6FloatAlloc { this[0] = 0.79335334f; this[1] = 0.92387953f; this[2] = 0.99144486f; this[3] = 0.60876143f; this[4] = 0.38268343f; this[5] = 0.13052619f } }()
    private var __STATIC_L3_imdct_gr_g_mdct_window: Array2Array18Float = { Array2Array18FloatAlloc { this[0] = Array18FloatAlloc { this[0] = 0.99904822f; this[1] = 0.99144486f; this[2] = 0.97629601f; this[3] = 0.95371695f; this[4] = 0.92387953f; this[5] = 0.88701083f; this[6] = 0.84339145f; this[7] = 0.79335334f; this[8] = 0.73727734f; this[9] = 0.04361938f; this[10] = 0.13052619f; this[11] = 0.21643961f; this[12] = 0.3007058f; this[13] = 0.38268343f; this[14] = 0.46174861f; this[15] = 0.53729961f; this[16] = 0.60876143f; this[17] = 0.67559021f }; this[1] = Array18FloatAlloc { this[0] = 1f; this[1] = 1f; this[2] = 1f; this[3] = 1f; this[4] = 1f; this[5] = 1f; this[6] = 0.99144486f; this[7] = 0.92387953f; this[8] = 0.79335334f; this[9] = 0f; this[10] = 0f; this[11] = 0f; this[12] = 0f; this[13] = 0f; this[14] = 0f; this[15] = 0.13052619f; this[16] = 0.38268343f; this[17] = 0.60876143f } } }()
    private var __STATIC_mp3d_DCT_II_g_sec: Array24Float = { Array24FloatAlloc { this[0] = 10.19000816f; this[1] = 0.50060302f; this[2] = 0.50241929f; this[3] = 3.40760851f; this[4] = 0.50547093f; this[5] = 0.52249861f; this[6] = 2.05778098f; this[7] = 0.51544732f; this[8] = 0.56694406f; this[9] = 1.4841646f; this[10] = 0.53104258f; this[11] = 0.6468218f; this[12] = 1.16943991f; this[13] = 0.55310392f; this[14] = 0.7881546f; this[15] = 0.97256821f; this[16] = 0.58293498f; this[17] = 1.06067765f; this[18] = 0.83934963f; this[19] = 0.62250412f; this[20] = 1.72244716f; this[21] = 0.74453628f; this[22] = 0.67480832f; this[23] = 5.10114861f } }()
    private var __STATIC_mp3d_synth_g_win: CPointer<Float> = { fixedArrayOfFloat(240) { this[0] = -1f; this[1] = 26f; this[2] = -31f; this[3] = 208f; this[4] = 218f; this[5] = 401f; this[6] = -519f; this[7] = 2063f; this[8] = 2000f; this[9] = 4788f; this[10] = -5517f; this[11] = 7134f; this[12] = 5959f; this[13] = 35640f; this[14] = -39336f; this[15] = 74992f; this[16] = -1f; this[17] = 24f; this[18] = -35f; this[19] = 202f; this[20] = 222f; this[21] = 347f; this[22] = -581f; this[23] = 2080f; this[24] = 1952f; this[25] = 4425f; this[26] = -5879f; this[27] = 7640f; this[28] = 5288f; this[29] = 33791f; this[30] = -41176f; this[31] = 74856f; this[32] = -1f; this[33] = 21f; this[34] = -38f; this[35] = 196f; this[36] = 225f; this[37] = 294f; this[38] = -645f; this[39] = 2087f; this[40] = 1893f; this[41] = 4063f; this[42] = -6237f; this[43] = 8092f; this[44] = 4561f; this[45] = 31947f; this[46] = -43006f; this[47] = 74630f; this[48] = -1f; this[49] = 19f; this[50] = -41f; this[51] = 190f; this[52] = 227f; this[53] = 244f; this[54] = -711f; this[55] = 2085f; this[56] = 1822f; this[57] = 3705f; this[58] = -6589f; this[59] = 8492f; this[60] = 3776f; this[61] = 30112f; this[62] = -44821f; this[63] = 74313f; this[64] = -1f; this[65] = 17f; this[66] = -45f; this[67] = 183f; this[68] = 228f; this[69] = 197f; this[70] = -779f; this[71] = 2075f; this[72] = 1739f; this[73] = 3351f; this[74] = -6935f; this[75] = 8840f; this[76] = 2935f; this[77] = 28289f; this[78] = -46617f; this[79] = 73908f; this[80] = -1f; this[81] = 16f; this[82] = -49f; this[83] = 176f; this[84] = 228f; this[85] = 153f; this[86] = -848f; this[87] = 2057f; this[88] = 1644f; this[89] = 3004f; this[90] = -7271f; this[91] = 9139f; this[92] = 2037f; this[93] = 26482f; this[94] = -48390f; this[95] = 73415f; this[96] = -2f; this[97] = 14f; this[98] = -53f; this[99] = 169f; this[100] = 227f; this[101] = 111f; this[102] = -919f; this[103] = 2032f; this[104] = 1535f; this[105] = 2663f; this[106] = -7597f; this[107] = 9389f; this[108] = 1082f; this[109] = 24694f; this[110] = -50137f; this[111] = 72835f; this[112] = -2f; this[113] = 13f; this[114] = -58f; this[115] = 161f; this[116] = 224f; this[117] = 72f; this[118] = -991f; this[119] = 2001f; this[120] = 1414f; this[121] = 2330f; this[122] = -7910f; this[123] = 9592f; this[124] = 70f; this[125] = 22929f; this[126] = -51853f; this[127] = 72169f; this[128] = -2f; this[129] = 11f; this[130] = -63f; this[131] = 154f; this[132] = 221f; this[133] = 36f; this[134] = -1064f; this[135] = 1962f; this[136] = 1280f; this[137] = 2006f; this[138] = -8209f; this[139] = 9750f; this[140] = -998f; this[141] = 21189f; this[142] = -53534f; this[143] = 71420f; this[144] = -2f; this[145] = 10f; this[146] = -68f; this[147] = 147f; this[148] = 215f; this[149] = 2f; this[150] = -1137f; this[151] = 1919f; this[152] = 1131f; this[153] = 1692f; this[154] = -8491f; this[155] = 9863f; this[156] = -2122f; this[157] = 19478f; this[158] = -55178f; this[159] = 70590f; this[160] = -3f; this[161] = 9f; this[162] = -73f; this[163] = 139f; this[164] = 208f; this[165] = -29f; this[166] = -1210f; this[167] = 1870f; this[168] = 970f; this[169] = 1388f; this[170] = -8755f; this[171] = 9935f; this[172] = -3300f; this[173] = 17799f; this[174] = -56778f; this[175] = 69679f; this[176] = -3f; this[177] = 8f; this[178] = -79f; this[179] = 132f; this[180] = 200f; this[181] = -57f; this[182] = -1283f; this[183] = 1817f; this[184] = 794f; this[185] = 1095f; this[186] = -8998f; this[187] = 9966f; this[188] = -4533f; this[189] = 16155f; this[190] = -58333f; this[191] = 68692f; this[192] = -4f; this[193] = 7f; this[194] = -85f; this[195] = 125f; this[196] = 189f; this[197] = -83f; this[198] = -1356f; this[199] = 1759f; this[200] = 605f; this[201] = 814f; this[202] = -9219f; this[203] = 9959f; this[204] = -5818f; this[205] = 14548f; this[206] = -59838f; this[207] = 67629f; this[208] = -4f; this[209] = 7f; this[210] = -91f; this[211] = 117f; this[212] = 177f; this[213] = -106f; this[214] = -1428f; this[215] = 1698f; this[216] = 402f; this[217] = 545f; this[218] = -9416f; this[219] = 9916f; this[220] = -7154f; this[221] = 12980f; this[222] = -61289f; this[223] = 66494f; this[224] = -5f; this[225] = 6f; this[226] = -97f; this[227] = 111f; this[228] = 163f; this[229] = -127f; this[230] = -1498f; this[231] = 1634f; this[232] = 185f; this[233] = 288f; this[234] = -9585f; this[235] = 9838f; this[236] = -8540f; this[237] = 11455f; this[238] = -62684f; this[239] = 65290f } }()
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
    // var malloc: CFunction1<Int, CPointer<Unit>> = 0 /*CFunction1<size_t, CPointer<Unit>>*/
    // var realloc: CFunction2<CPointer<Unit>, Int, CPointer<Unit>> = 0 /*CFunction2<CPointer<Unit>, size_t, CPointer<Unit>>*/
    // var free: CFunction1<CPointer<Unit>, Unit> = 0 /*CFunction1<CPointer<Unit>, Unit>*/
    // var exit: CFunction1<Int, Unit> = 0 /*CFunction1<Int, Unit>*/
    // var strlen: CFunction1<CPointer<Byte>, Int> = 0 /*CFunction1<CPointer<Byte>, size_t>*/
    // var memset: CFunction3<CPointer<Unit>, Int, Int, CPointer<Unit>> = 0 /*CFunction3<CPointer<Unit>, Int, size_t, CPointer<Unit>>*/
    // var memcpy: CFunction3<CPointer<Unit>, CPointer<Unit>, Int, CPointer<Unit>> = 0 /*CFunction3<CPointer<Unit>, CPointer<Unit>, size_t, CPointer<Unit>>*/
    // var memmove: CFunction3<CPointer<Unit>, CPointer<Unit>, Int, CPointer<Unit>> = 0 /*CFunction3<CPointer<Unit>, CPointer<Unit>, size_t, CPointer<Unit>>*/
    // var memcmp: CFunction3<CPointer<Unit>, CPointer<Unit>, Int, Int> = 0 /*CFunction3<CPointer<Unit>, CPointer<Unit>, size_t, Int>*/
    // typealias bs_t = bs_t
    // typealias L12_scale_info = L12_scale_info
    // typealias L12_subband_alloc_t = L12_subband_alloc_t
    // typealias L3_gr_info_t = L3_gr_info_t
    // typealias mp3dec_scratch_t = mp3dec_scratch_t
    fun bs_init(bs: CPointer<bs_t>, data: CPointer<UByte>, bytes: Int): Unit {
        bs.value.buf = data
        bs.value.pos = 0
        bs.value.limit = bytes * 8

    }
    fun get_bits(bs: CPointer<bs_t>, n: Int): UInt {
        var next: UInt = 0u
        var cache: UInt = 0u
        var s: UInt = (((bs.value.pos and 7)).toUInt())
        var shl: Int = (n + ((s).toInt()))
        var p: CPointer<UByte> = (bs.value.buf.plus((bs.value.pos shr 3)))
        if ((run { bs.value.pos + n }.also { `$` -> bs.value.pos = `$` }) > bs.value.limit) {
            return 0u
        }
        next = ((p.also { p = p.plus(1) }.value).toUInt()) and (((255 shr ((s).toInt()))).toUInt())
        while ((run { shl - 8 }.also { `$` -> shl = `$` }) > 0) {
            cache = cache or (next shl shl)
            next = (p.also { p = p.plus(1) }.value).toUInt()
        }
        return cache or (next shr (-shl))

    }
    fun hdr_valid(h: CPointer<UByte>): Int {
        return ((((((h[0] == ((255).toUByte())) && (((((((h[1]).toUInt()) and 240u)).toInt()) == 240) || ((((((h[1]).toUInt()) and 254u)).toInt()) == 226))) && (((((((h[1]).toUInt()) shr 1) and 3u)).toInt()) != 0)) && ((((((h[2]).toUInt()) shr 4)).toInt()) != 15)) && (((((((h[2]).toUInt()) shr 2) and 3u)).toInt()) != 3))).toInt().toInt()

    }
    fun hdr_compare(h1: CPointer<UByte>, h2: CPointer<UByte>): Int {
        return ((((((hdr_valid(h2)).toBool()) && (((((((h1[1]).toUInt()) xor ((h2[1]).toUInt())) and 254u)).toInt()) == 0)) && (((((((h1[2]).toUInt()) xor ((h2[2]).toUInt())) and 12u)).toInt()) == 0)) && (!(((((((((((h1[2]).toUInt()) and 240u)).toInt()) == 0)).toInt().toInt()) xor ((((((((h2[2]).toUInt()) and 240u)).toInt()) == 0)).toInt().toInt()))).toBool())))).toInt().toInt()

    }
    fun hdr_bitrate_kbps(h: CPointer<UByte>): UInt {
        var halfrate: Array2Array3Array15UByte = __STATIC_hdr_bitrate_kbps_halfrate
        return ((2 * ((halfrate[((!(!(((((h[1]).toUInt()) and 8u)).toBool())))).toInt().toInt()][((((((h[1]).toUInt()) shr 1) and 3u)).toInt()) - 1][((((h[2]).toUInt()) shr 4)).toInt()]).toInt()))).toUInt()

    }
    fun hdr_sample_rate_hz(h: CPointer<UByte>): UInt {
        var g_hz: Array3UInt = __STATIC_hdr_sample_rate_hz_g_hz
        return (g_hz[(((((h[2]).toUInt()) shr 2) and 3u)).toInt()] shr (((!(((((h[1]).toUInt()) and 8u)).toBool()))).toInt().toInt())) shr (((!(((((h[1]).toUInt()) and 16u)).toBool()))).toInt().toInt())

    }
    fun hdr_frame_samples(h: CPointer<UByte>): UInt {
        return ((if ((((((h[1]).toUInt()) and 6u)).toInt()) == 6) 384 else (1152 shr ((((((((h[1]).toUInt()) and 14u)).toInt()) == 2)).toInt().toInt())))).toUInt()

    }
    fun hdr_frame_bytes(h: CPointer<UByte>, free_format_size: Int): Int {
        var frame_bytes: Int = (((((hdr_frame_samples(h) * hdr_bitrate_kbps(h)) * 125u) / hdr_sample_rate_hz(h))).toInt())
        if ((((((h[1]).toUInt()) and 6u)).toInt()) == 6) {
            frame_bytes = frame_bytes and ((3).inv())
        }
        return (if ((frame_bytes).toBool()) frame_bytes else free_format_size)

    }
    fun hdr_padding(h: CPointer<UByte>): Int {
        return (if (((((h[2]).toUInt()) and 2u)).toBool()) (if ((((((h[1]).toUInt()) and 6u)).toInt()) == 6) 4 else 1) else 0)

    }
    fun L12_subband_alloc_table(hdr: CPointer<UByte>, sci: CPointer<L12_scale_info>): CPointer<L12_subband_alloc_t> {
        var alloc: CPointer<L12_subband_alloc_t> = CPointer(0)
        var mode: Int = ((((((hdr[3]).toUInt()) shr 6) and 3u)).toInt())
        var nbands: Int = 0
        var stereo_bands: Int = (if (mode == 3) 0 else (if (mode == 1) ((((((((hdr[3]).toUInt()) shr 4) and 3u) shl 2)).toInt()) + 4) else 32))
        if ((((((hdr[1]).toUInt()) and 6u)).toInt()) == 6) {
            run {
                var g_alloc_L1: CPointer<L12_subband_alloc_t> = __STATIC_L12_subband_alloc_table_g_alloc_L1
                alloc = CPointer<L12_subband_alloc_t>(((g_alloc_L1).ptr).toInt())
                nbands = 32

            }
        } else {
            if (!(((((hdr[1]).toUInt()) and 8u)).toBool())) {
                run {
                    var g_alloc_L2M2: CPointer<L12_subband_alloc_t> = __STATIC_L12_subband_alloc_table_g_alloc_L2M2
                    alloc = CPointer<L12_subband_alloc_t>(((g_alloc_L2M2).ptr).toInt())
                    nbands = 30

                }
            } else {
                run {
                    var g_alloc_L2M1: CPointer<L12_subband_alloc_t> = __STATIC_L12_subband_alloc_table_g_alloc_L2M1
                    var sample_rate_idx: Int = ((((((hdr[2]).toUInt()) shr 2) and 3u)).toInt())
                    var kbps: UInt = (hdr_bitrate_kbps(hdr) shr (((mode != 3)).toInt().toInt()))
                    if (!((kbps).toBool())) {
                        kbps = 192u
                    }
                    alloc = CPointer<L12_subband_alloc_t>(((g_alloc_L2M1).ptr).toInt())
                    nbands = 27
                    if (((kbps).toInt()) < 56) {
                        run {
                            var g_alloc_L2M1_lowrate: CPointer<L12_subband_alloc_t> = __STATIC_L12_subband_alloc_table_g_alloc_L2M1_lowrate
                            alloc = CPointer<L12_subband_alloc_t>(((g_alloc_L2M1_lowrate).ptr).toInt())
                            nbands = (if (sample_rate_idx == 2) 12 else 8)

                        }
                    } else {
                        if ((((kbps).toInt()) >= 96) && (sample_rate_idx != 1)) {
                            nbands = 30
                        }
                    }

                }
            }
        }
        sci.value.total_bands = (nbands).toUByte()
        sci.value.stereo_bands = ((if (stereo_bands > nbands) nbands else stereo_bands)).toUByte()
        return alloc

    }
    fun L12_read_scalefactors(bs: CPointer<bs_t>, pba: CPointer<UByte>, scfcod: CPointer<UByte>, bands: Int, scf: CPointer<Float>): Unit {
        var pba = pba // Mutating parameter
        var scf = scf // Mutating parameter
        var g_deq_L12: Array54Float = __STATIC_L12_read_scalefactors_g_deq_L12
        var i: Int = 0
        var m: Int = 0
        i = 0
        while (i < bands) {
            run {
                var s: Float = 0f
                var ba: Int = ((pba.also { pba = pba.plus(1) }.value).toInt())
                var mask: Int = (if ((ba).toBool()) (4 + ((19 shr ((scfcod[i]).toInt())) and 3)) else 0)
                m = 4
                while ((m).toBool()) {
                    if (((mask and m)).toBool()) {
                        run {
                            var b: Int = ((get_bits(bs, 6)).toInt())
                            s = g_deq_L12[((ba * 3) - 6) + (b % 3)] * ((((1 shl 21) shr (b / 3))).toFloat())

                        }
                    }
                    scf.also { scf = scf.plus(1) }.value = s
                    m = m shr 1
                }

            }
            i = i + 1
        }

    }
    fun L12_read_scale_info(hdr: CPointer<UByte>, bs: CPointer<bs_t>, sci: CPointer<L12_scale_info>): Unit {
        var g_bitalloc_code_tab: CPointer<UByte> = __STATIC_L12_read_scale_info_g_bitalloc_code_tab
        var subband_alloc: CPointer<L12_subband_alloc_t> = L12_subband_alloc_table(hdr, sci)
        var i: Int = 0
        var k: Int = 0
        var ba_bits: Int = 0
        var ba_code_tab: CPointer<UByte> = (CPointer<UByte>(((g_bitalloc_code_tab).ptr).toInt()))
        i = 0
        while (i < ((sci.value.total_bands).toInt())) {
            run {
                var ba: UByte = 0u
                if (i == k) {
                    k = k + ((subband_alloc.value.band_count).toInt())
                    ba_bits = (subband_alloc.value.code_tab_width).toInt()
                    ba_code_tab = CPointer<UByte>((((g_bitalloc_code_tab.plus(((subband_alloc.value.tab_offset).toInt())))).ptr).toInt())
                    subband_alloc = subband_alloc.plus(1)
                }
                ba = ba_code_tab[(get_bits(bs, ba_bits)).toInt()]
                sci.value.bitalloc[2 * i] = ba
                if (i < ((sci.value.stereo_bands).toInt())) {
                    ba = ba_code_tab[(get_bits(bs, ba_bits)).toInt()]
                }
                sci.value.bitalloc[(2 * i) + 1] = (if ((sci.value.stereo_bands).toBool()) ba else ((0).toUByte()))

            }
            i = i + 1
        }
        i = 0
        while (i < (2 * ((sci.value.total_bands).toInt()))) {
            sci.value.scfcod[i] = ((if ((sci.value.bitalloc[i]).toBool()) (if ((((((hdr[1]).toUInt()) and 6u)).toInt()) == 6) 2 else ((get_bits(bs, 2)).toInt())) else 6)).toUByte()
            i = i + 1
        }
        L12_read_scalefactors(bs, (CPointer<UByte>(((sci.value.bitalloc).ptr).toInt())), (CPointer<UByte>(((sci.value.scfcod).ptr).toInt())), (((((sci.value.total_bands).toUInt()) * 2u)).toInt()), (CPointer<Float>(((sci.value.scf).ptr).toInt())))
        i = (sci.value.stereo_bands).toInt()
        while (i < ((sci.value.total_bands).toInt())) {
            sci.value.bitalloc[(2 * i) + 1] = (0).toUByte()
            i = i + 1
        }

    }
    fun L12_dequantize_granule(grbuf: CPointer<Float>, bs: CPointer<bs_t>, sci: CPointer<L12_scale_info>, group_size: Int): Int {
        var i: Int = 0
        var j: Int = 0
        var k: Int = 0
        var choff: Int = 576
        j = 0
        while (j < 4) {
            run {
                var dst: CPointer<Float> = (grbuf.plus((group_size * j)))
                i = 0
                while (i < (2 * ((sci.value.total_bands).toInt()))) {
                    run {
                        var ba: Int = ((sci.value.bitalloc[i]).toInt())
                        if (ba != 0) {
                            if (ba < 17) {
                                run {
                                    var half: Int = ((1 shl (ba - 1)) - 1)
                                    k = 0
                                    while (k < group_size) {
                                        dst[k] = ((((get_bits(bs, ba)).toInt()) - half)).toFloat()
                                        k = k + 1
                                    }

                                }
                            } else {
                                run {
                                    var mod: UInt = ((((2 shl (ba - 17)) + 1)).toUInt())
                                    var code: UInt = get_bits(bs, ((((mod).toInt()) + 2) - (((mod shr 3)).toInt())))
                                    k = 0
                                    while (k < group_size) {
                                        dst[k] = (((((code % mod) - (mod / 2u))).toInt())).toFloat()
                                        run { k++; run { code / mod }.also { `$` -> code = `$` } }
                                    }

                                }
                            }
                        }
                        dst = dst.plus(choff)
                        choff = 18 - choff

                    }
                    i = i + 1
                }

            }
            j = j + 1
        }
        return group_size * 4

    }
    fun L12_apply_scf_384(sci: CPointer<L12_scale_info>, scf: CPointer<Float>, dst: CPointer<Float>): Unit {
        var scf = scf // Mutating parameter
        var dst = dst // Mutating parameter
        var i: Int = 0
        var k: Int = 0
        memcpy((CPointer<Unit>(((((dst.plus(576)).plus((((((sci.value.stereo_bands).toUInt()) * 18u)).toInt())))).ptr).toInt())), (CPointer<Unit>((((dst.plus((((((sci.value.stereo_bands).toUInt()) * 18u)).toInt())))).ptr).toInt())), (((((((sci.value.total_bands).toUInt()) - ((sci.value.stereo_bands).toUInt())) * 18u) * ((Float.SIZE_BYTES).toUInt()))).toInt()))
        i = 0
        while (i < ((sci.value.total_bands).toInt())) {
            k = 0
            while (k < 12) {
                dst[k + 0] = dst[k + 0] * scf[0]
                dst[k + 576] = dst[k + 576] * scf[3]
                k = k + 1
            }
            run { i++; run { dst.plus(18) }.also { `$` -> dst = `$` }; run { scf.plus(6) }.also { `$` -> scf = `$` } }
        }

    }
    fun L3_read_side_info(bs: CPointer<bs_t>, gr: CPointer<L3_gr_info_t>, hdr: CPointer<UByte>): Int {
        var gr = gr // Mutating parameter
        var g_scf_long: Array8Array23UByte = __STATIC_L3_read_side_info_g_scf_long
        var g_scf_short: Array8Array40UByte = __STATIC_L3_read_side_info_g_scf_short
        var g_scf_mixed: Array8Array40UByte = __STATIC_L3_read_side_info_g_scf_mixed
        var tables: UInt = 0u
        var scfsi: UInt = 0u
        var main_data_begin: Int = 0
        var part_23_sum: Int = 0
        var sr_idx: Int = (((((((hdr[2]).toUInt()) shr 2) and 3u) + ((((((hdr[1]).toUInt()) shr 3) and 1u) + ((((hdr[1]).toUInt()) shr 4) and 1u)) * 3u))).toInt())
        sr_idx = sr_idx - (((sr_idx != 0)).toInt().toInt())
        var gr_count: Int = (if ((((((hdr[3]).toUInt()) and 192u)).toInt()) == 192) 1 else 2)
        if (((((hdr[1]).toUInt()) and 8u)).toBool()) {
            gr_count = gr_count * 2
            main_data_begin = (get_bits(bs, 9)).toInt()
            scfsi = get_bits(bs, (7 + gr_count))
        } else {
            main_data_begin = ((get_bits(bs, (8 + gr_count)) shr gr_count)).toInt()
        }
        do0@do {
            if ((((((hdr[3]).toUInt()) and 192u)).toInt()) == 192) {
                scfsi = scfsi shl 4
            }
            gr.value.part_23_length = (get_bits(bs, 12)).toUShort()
            part_23_sum = part_23_sum + ((gr.value.part_23_length).toInt())
            gr.value.big_values = (get_bits(bs, 9)).toUShort()
            if (gr.value.big_values > ((288).toUShort())) {
                return -1
            }
            gr.value.global_gain = (get_bits(bs, 8)).toUByte()
            gr.value.scalefac_compress = (get_bits(bs, (if (((((hdr[1]).toUInt()) and 8u)).toBool()) 4 else 9))).toUShort()
            gr.value.sfbtab = CPointer<UByte>(((g_scf_long[sr_idx]).ptr).toInt())
            gr.value.n_long_sfb = (22).toUByte()
            gr.value.n_short_sfb = (0).toUByte()
            if ((get_bits(bs, 1)).toBool()) {
                gr.value.block_type = (get_bits(bs, 2)).toUByte()
                if (!((gr.value.block_type).toBool())) {
                    return -1
                }
                gr.value.mixed_block_flag = (get_bits(bs, 1)).toUByte()
                gr.value.region_count[0] = (7).toUByte()
                gr.value.region_count[1] = (255).toUByte()
                if (gr.value.block_type == ((2).toUByte())) {
                    scfsi = scfsi and 3855u
                    if (!((gr.value.mixed_block_flag).toBool())) {
                        gr.value.region_count[0] = (8).toUByte()
                        gr.value.sfbtab = CPointer<UByte>(((g_scf_short[sr_idx]).ptr).toInt())
                        gr.value.n_long_sfb = (0).toUByte()
                        gr.value.n_short_sfb = (39).toUByte()
                    } else {
                        gr.value.sfbtab = CPointer<UByte>(((g_scf_mixed[sr_idx]).ptr).toInt())
                        gr.value.n_long_sfb = ((if (((((hdr[1]).toUInt()) and 8u)).toBool()) 8 else 6)).toUByte()
                        gr.value.n_short_sfb = (30).toUByte()
                    }
                }
                tables = get_bits(bs, 10)
                tables = tables shl 5
                gr.value.subblock_gain[0] = (get_bits(bs, 3)).toUByte()
                gr.value.subblock_gain[1] = (get_bits(bs, 3)).toUByte()
                gr.value.subblock_gain[2] = (get_bits(bs, 3)).toUByte()
            } else {
                gr.value.block_type = (0).toUByte()
                gr.value.mixed_block_flag = (0).toUByte()
                tables = get_bits(bs, 15)
                gr.value.region_count[0] = (get_bits(bs, 4)).toUByte()
                gr.value.region_count[1] = (get_bits(bs, 3)).toUByte()
                gr.value.region_count[2] = (255).toUByte()
            }
            gr.value.table_select[0] = ((tables shr 10)).toUByte()
            gr.value.table_select[1] = (((tables shr 5) and 31u)).toUByte()
            gr.value.table_select[2] = ((tables and 31u)).toUByte()
            gr.value.preflag = ((if (((((hdr[1]).toUInt()) and 8u)).toBool()) get_bits(bs, 1) else (((gr.value.scalefac_compress >= ((500).toUShort()))).toInt().toUInt()))).toUByte()
            gr.value.scalefac_scale = (get_bits(bs, 1)).toUByte()
            gr.value.count1_table = (get_bits(bs, 1)).toUByte()
            gr.value.scfsi = (((scfsi shr 12) and 15u)).toUByte()
            scfsi = scfsi shl 4
            gr = gr.plus(1)
        } while (((--gr_count)).toBool())
        if ((part_23_sum + bs.value.pos) > (bs.value.limit + (main_data_begin * 8))) {
            return -1
        }
        return main_data_begin

    }
    fun L3_read_scalefactors(scf: CPointer<UByte>, ist_pos: CPointer<UByte>, scf_size: CPointer<UByte>, scf_count: CPointer<UByte>, bitbuf: CPointer<bs_t>, scfsi: Int): Unit {
        var scf = scf // Mutating parameter
        var ist_pos = ist_pos // Mutating parameter
        var scfsi = scfsi // Mutating parameter
        var i: Int = 0
        var k: Int = 0
        i = 0
        while ((i < 4) && ((scf_count[i]).toBool())) {
            run {
                var cnt: Int = ((scf_count[i]).toInt())
                if (((scfsi and 8)).toBool()) {
                    memcpy((CPointer<Unit>(((scf).ptr).toInt())), (CPointer<Unit>(((ist_pos).ptr).toInt())), cnt)
                } else {
                    run {
                        var bits: Int = ((scf_size[i]).toInt())
                        if (!((bits).toBool())) {
                            memset((CPointer<Unit>(((scf).ptr).toInt())), 0, cnt)
                            memset((CPointer<Unit>(((ist_pos).ptr).toInt())), 0, cnt)
                        } else {
                            run {
                                var max_scf: Int = (((if (scfsi < 0) ((((1 shl bits) - 1)).toLong()) else -1L)).toInt())
                                k = 0
                                while (k < cnt) {
                                    run {
                                        var s: Int = ((get_bits(bitbuf, bits)).toInt())
                                        ist_pos[k] = ((if (s == max_scf) -1L else ((s).toLong()))).toUByte()
                                        scf[k] = (s).toUByte()

                                    }
                                    k = k + 1
                                }

                            }
                        }

                    }
                }
                ist_pos = ist_pos.plus(cnt)
                scf = scf.plus(cnt)

            }
            run { i++; run { scfsi * 2 }.also { `$` -> scfsi = `$` } }
        }
        scf[0] = run { run { (0).toUByte() }.also { `$` -> scf[2] = `$` } }.also { `$` -> scf[1] = `$` }

    }
    fun L3_ldexp_q2(y: Float, exp_q2: Int): Float {
        var y = y // Mutating parameter
        var exp_q2 = exp_q2 // Mutating parameter
        var g_expfrac: Array4Float = __STATIC_L3_ldexp_q2_g_expfrac
        var e: Int = 0
        do0@do {
            e = (if ((30 * 4) > exp_q2) exp_q2 else (30 * 4))
            y = y * (g_expfrac[e and 3] * ((((1 shl 30) shr (e shr 2))).toFloat()))
        } while ((run { exp_q2 - e }.also { `$` -> exp_q2 = `$` }) > 0)
        return y

    }
    fun L3_decode_scalefactors(hdr: CPointer<UByte>, ist_pos: CPointer<UByte>, bs: CPointer<bs_t>, gr: CPointer<L3_gr_info_t>, scf: CPointer<Float>, ch: Int): Unit = stackFrame {
        var g_scf_partitions: Array3Array28UByte = __STATIC_L3_decode_scalefactors_g_scf_partitions
        var scf_partition: CPointer<UByte> = (CPointer<UByte>(((g_scf_partitions[(((!(!((gr.value.n_short_sfb).toBool())))).toInt().toInt()) + (((!((gr.value.n_long_sfb).toBool()))).toInt().toInt())]).ptr).toInt()))
        var scf_size: Array4UByte = Array4UByteAlloc { this[0] = ((0).toUByte()) }
        var iscf: Array40UByte = Array40UByteAlloc { this[0] = ((0).toUByte()) }
        var i: Int = 0
        var scf_shift: Int = (((((gr.value.scalefac_scale).toUInt()) + 1u)).toInt())
        var gain_exp: Int = 0
        var scfsi: Int = ((gr.value.scfsi).toInt())
        var gain: Float = 0f
        if (((((hdr[1]).toUInt()) and 8u)).toBool()) {
            run {
                var g_scfc_decode: Array16UByte = __STATIC_L3_decode_scalefactors_g_scfc_decode
                var part: Int = ((g_scfc_decode[(gr.value.scalefac_compress).toInt()]).toInt())
                scf_size[1] = run { ((part shr 2)).toUByte() }.also { `$` -> scf_size[0] = `$` }
                scf_size[3] = run { ((part and 3)).toUByte() }.also { `$` -> scf_size[2] = `$` }

            }
        } else {
            run {
                var g_mod: Array24UByte = __STATIC_L3_decode_scalefactors_g_mod
                var k: Int = 0
                var modprod: Int = 0
                var sfc: Int = 0
                var ist: Int = ((((((((hdr[3]).toUInt()) and 16u)).toBool()) && ((ch).toBool()))).toInt().toInt())
                sfc = ((((gr.value.scalefac_compress).toUInt()) shr ist)).toInt()
                k = (ist * 3) * 4
                while (sfc >= 0) {
                    run { run { 1 }.also { `$` -> modprod = `$` }; run { 3 }.also { `$` -> i = `$` } }
                    while (i >= 0) {
                        scf_size[i] = (((sfc / modprod) % ((g_mod[k + i]).toInt()))).toUByte()
                        modprod = modprod * ((g_mod[k + i]).toInt())
                        i = i - 1
                    }
                    run { run { sfc - modprod }.also { `$` -> sfc = `$` }; run { k + 4 }.also { `$` -> k = `$` } }
                }
                scf_partition = scf_partition.plus(k)
                scfsi = -16

            }
        }
        L3_read_scalefactors((CPointer<UByte>(((iscf).ptr).toInt())), ist_pos, (CPointer<UByte>(((scf_size).ptr).toInt())), scf_partition, bs, scfsi)
        if ((gr.value.n_short_sfb).toBool()) {
            run {
                var sh: Int = (3 - scf_shift)
                i = 0
                while (i < ((gr.value.n_short_sfb).toInt())) {
                    iscf[(((((gr.value.n_long_sfb).toUInt()) + ((i).toUInt()))).toInt()) + 0] = ((((iscf[(((((gr.value.n_long_sfb).toUInt()) + ((i).toUInt()))).toInt()) + 0]).toUInt()) + (((gr.value.subblock_gain[0]).toUInt()) shl sh))).toUByte()
                    iscf[(((((gr.value.n_long_sfb).toUInt()) + ((i).toUInt()))).toInt()) + 1] = ((((iscf[(((((gr.value.n_long_sfb).toUInt()) + ((i).toUInt()))).toInt()) + 1]).toUInt()) + (((gr.value.subblock_gain[1]).toUInt()) shl sh))).toUByte()
                    iscf[(((((gr.value.n_long_sfb).toUInt()) + ((i).toUInt()))).toInt()) + 2] = ((((iscf[(((((gr.value.n_long_sfb).toUInt()) + ((i).toUInt()))).toInt()) + 2]).toUInt()) + (((gr.value.subblock_gain[2]).toUInt()) shl sh))).toUByte()
                    i = i + 3
                }

            }
        } else {
            if ((gr.value.preflag).toBool()) {
                run {
                    var g_preamp: Array10UByte = __STATIC_L3_decode_scalefactors_g_preamp
                    i = 0
                    while (i < 10) {
                        iscf[11 + i] = ((((iscf[11 + i]).toUInt()) + ((g_preamp[i]).toUInt()))).toUByte()
                        i = i + 1
                    }

                }
            }
        }
        gain_exp = ((((((gr.value.global_gain).toUInt()) + (((-1L * 4L)).toUInt()))).toInt()) - 210) - (if ((((((hdr[3]).toUInt()) and 224u)).toInt()) == 96) 2 else 0)
        gain = L3_ldexp_q2((((1 shl (((((((255L + (-1L * 4L)) - 210L) + 3L) and ((((3).inv())).toLong())) / 4L)).toInt()))).toFloat()), (((((((255L + (-1L * 4L)) - 210L) + 3L) and ((((3).inv())).toLong())) - ((gain_exp).toLong()))).toInt()))
        i = 0
        while (i < (((((gr.value.n_long_sfb).toUInt()) + ((gr.value.n_short_sfb).toUInt()))).toInt())) {
            scf[i] = L3_ldexp_q2(gain, (((((iscf[i]).toUInt()) shl scf_shift)).toInt()))
            i = i + 1
        }

    }
    var g_pow43: Array145Float /*static*/ = Array145FloatAlloc { this[0] = 0f; this[1] = -1f; this[2] = -2.519842f; this[3] = -4.326749f; this[4] = -6.349604f; this[5] = -8.54988f; this[6] = -10.902724f; this[7] = -13.390518f; this[8] = -16f; this[9] = -18.720754f; this[10] = -21.544347f; this[11] = -24.463781f; this[12] = -27.473142f; this[13] = -30.567351f; this[14] = -33.741992f; this[15] = -36.993181f; this[16] = 0f; this[17] = 1f; this[18] = 2.519842f; this[19] = 4.326749f; this[20] = 6.349604f; this[21] = 8.54988f; this[22] = 10.902724f; this[23] = 13.390518f; this[24] = 16f; this[25] = 18.720754f; this[26] = 21.544347f; this[27] = 24.463781f; this[28] = 27.473142f; this[29] = 30.567351f; this[30] = 33.741992f; this[31] = 36.993181f; this[32] = 40.317474f; this[33] = 43.711787f; this[34] = 47.173345f; this[35] = 50.699631f; this[36] = 54.288352f; this[37] = 57.937408f; this[38] = 61.644865f; this[39] = 65.408941f; this[40] = 69.227979f; this[41] = 73.100443f; this[42] = 77.024898f; this[43] = 81f; this[44] = 85.024491f; this[45] = 89.097188f; this[46] = 93.216975f; this[47] = 97.3828f; this[48] = 101.593667f; this[49] = 105.848633f; this[50] = 110.146801f; this[51] = 114.487321f; this[52] = 118.869381f; this[53] = 123.292209f; this[54] = 127.755065f; this[55] = 132.257246f; this[56] = 136.798076f; this[57] = 141.376907f; this[58] = 145.993119f; this[59] = 150.646117f; this[60] = 155.335327f; this[61] = 160.060199f; this[62] = 164.820202f; this[63] = 169.614826f; this[64] = 174.443577f; this[65] = 179.30598f; this[66] = 184.201575f; this[67] = 189.129918f; this[68] = 194.09058f; this[69] = 199.083145f; this[70] = 204.10721f; this[71] = 209.162385f; this[72] = 214.248292f; this[73] = 219.364564f; this[74] = 224.510845f; this[75] = 229.686789f; this[76] = 234.892058f; this[77] = 240.126328f; this[78] = 245.38928f; this[79] = 250.680604f; this[80] = 256f; this[81] = 261.347174f; this[82] = 266.721841f; this[83] = 272.123723f; this[84] = 277.552547f; this[85] = 283.008049f; this[86] = 288.489971f; this[87] = 293.99806f; this[88] = 299.532071f; this[89] = 305.091761f; this[90] = 310.676898f; this[91] = 316.287249f; this[92] = 321.922592f; this[93] = 327.582707f; this[94] = 333.267377f; this[95] = 338.976394f; this[96] = 344.70955f; this[97] = 350.466646f; this[98] = 356.247482f; this[99] = 362.051866f; this[100] = 367.879608f; this[101] = 373.730522f; this[102] = 379.604427f; this[103] = 385.501143f; this[104] = 391.420496f; this[105] = 397.362314f; this[106] = 403.326427f; this[107] = 409.312672f; this[108] = 415.320884f; this[109] = 421.350905f; this[110] = 427.402579f; this[111] = 433.47575f; this[112] = 439.570269f; this[113] = 445.685987f; this[114] = 451.822757f; this[115] = 457.980436f; this[116] = 464.158883f; this[117] = 470.35796f; this[118] = 476.57753f; this[119] = 482.817459f; this[120] = 489.077615f; this[121] = 495.357868f; this[122] = 501.65809f; this[123] = 507.978156f; this[124] = 514.317941f; this[125] = 520.677324f; this[126] = 527.056184f; this[127] = 533.454404f; this[128] = 539.871867f; this[129] = 546.308458f; this[130] = 552.764065f; this[131] = 559.238575f; this[132] = 565.731879f; this[133] = 572.24387f; this[134] = 578.77444f; this[135] = 585.323483f; this[136] = 591.890898f; this[137] = 598.476581f; this[138] = 605.080431f; this[139] = 611.702349f; this[140] = 618.342238f; this[141] = 625f; this[142] = 631.67554f; this[143] = 638.368763f; this[144] = 645.079578f }
    fun L3_pow_43(x: Int): Float {
        var x = x // Mutating parameter
        var frac: Float = 0f
        var sign: Int = 0
        var mult: Int = 256
        if (x < 129) {
            return g_pow43[16 + x]
        }
        if (x < 1024) {
            mult = 16
            x = x shl 3
        }
        sign = (2 * x) and 64
        frac = ((((x and 63) - sign)).toFloat()) / ((((x and ((63).inv())) + sign)).toFloat())
        return (g_pow43[16 + ((x + sign) shr 6)] * (1f + (frac * ((4f / 3f) + (frac * (2f / 9f)))))) * ((mult).toFloat())

    }
    fun L3_huffman(dst: CPointer<Float>, bs: CPointer<bs_t>, gr_info: CPointer<L3_gr_info_t>, scf: CPointer<Float>, layer3gr_limit: Int): Unit {
        var dst = dst // Mutating parameter
        var scf = scf // Mutating parameter
        var tabs: CPointer<Short> = __STATIC_L3_huffman_tabs
        var tab32: CPointer<UByte> = __STATIC_L3_huffman_tab32
        var tab33: CPointer<UByte> = __STATIC_L3_huffman_tab33
        var tabindex: Array32Short = __STATIC_L3_huffman_tabindex
        var g_linbits: CPointer<UByte> = __STATIC_L3_huffman_g_linbits
        var one: Float = 0f
        var ireg: Int = 0
        var big_val_cnt: Int = ((gr_info.value.big_values).toInt())
        var sfb: CPointer<UByte> = gr_info.value.sfbtab
        var bs_next_ptr: CPointer<UByte> = (bs.value.buf.plus((bs.value.pos / 8)))
        var bs_cache: UInt = (((((((((bs_next_ptr[0]).toUInt()) * 256u) + ((bs_next_ptr[1]).toUInt())) * 256u) + ((bs_next_ptr[2]).toUInt())) * 256u) + ((bs_next_ptr[3]).toUInt())) shl (bs.value.pos and 7))
        var pairs_to_decode: Int = 0
        var np: Int = 0
        var bs_sh: Int = ((bs.value.pos and 7) - 8)
        bs_next_ptr = bs_next_ptr.plus(4)
        while (big_val_cnt > 0) {
            run {
                var tab_num: Int = ((gr_info.value.table_select[ireg]).toInt())
                var sfb_cnt: Int = ((gr_info.value.region_count[ireg++]).toInt())
                var codebook: CPointer<Short> = (CPointer<Short>((((tabs.plus(((tabindex[tab_num]).toInt())))).ptr).toInt()))
                var linbits: Int = ((g_linbits[tab_num]).toInt())
                if ((linbits).toBool()) {
                    do0@do {
                        np = ((((sfb.also { sfb = sfb.plus(1) }.value).toUInt()) / 2u)).toInt()
                        pairs_to_decode = (if (big_val_cnt > np) np else big_val_cnt)
                        one = scf.also { scf = scf.plus(1) }.value
                        do1@do {
                            run {
                                var j: Int = 0
                                var w: Int = 5
                                var leaf: Int = ((codebook[((bs_cache shr (32 - w))).toInt()]).toInt())
                                while (leaf < 0) {
                                    bs_cache = bs_cache shl w
                                    bs_sh = bs_sh + w
                                    w = leaf and 7
                                    leaf = (codebook[(((bs_cache shr (32 - w))).toInt()) - (leaf shr 3)]).toInt()
                                }
                                bs_cache = bs_cache shl (leaf shr 8)
                                bs_sh = bs_sh + (leaf shr 8)
                                j = 0
                                while (j < 2) {
                                    run {
                                        var lsb: Int = (leaf and 15)
                                        if (lsb == 15) {
                                            lsb = lsb + (((bs_cache shr (32 - linbits))).toInt())
                                            bs_cache = bs_cache shl linbits
                                            bs_sh = bs_sh + linbits
                                            while (bs_sh >= 0) {
                                                bs_cache = bs_cache or (((bs_next_ptr.also { bs_next_ptr = bs_next_ptr.plus(1) }.value).toUInt()) shl bs_sh)
                                                bs_sh = bs_sh - 8
                                            }
                                            dst.value = (one * L3_pow_43(lsb)) * (((if (((bs_cache).toInt()) < 0) -1L else 1L)).toFloat())
                                        } else {
                                            dst.value = g_pow43[(16 + lsb) - (16 * (((bs_cache shr 31)).toInt()))] * one
                                        }
                                        bs_cache = bs_cache shl (if ((lsb).toBool()) 1 else 0)
                                        bs_sh = bs_sh + (if ((lsb).toBool()) 1 else 0)

                                    }
                                    run { j++; dst.also { dst = dst.plus(1) }; run { leaf shr 4 }.also { `$` -> leaf = `$` } }
                                }
                                while (bs_sh >= 0) {
                                    bs_cache = bs_cache or (((bs_next_ptr.also { bs_next_ptr = bs_next_ptr.plus(1) }.value).toUInt()) shl bs_sh)
                                    bs_sh = bs_sh - 8
                                }

                            }
                        } while (((--pairs_to_decode)).toBool())
                    } while (((run { big_val_cnt - np }.also { `$` -> big_val_cnt = `$` }) > 0) && ((--sfb_cnt) >= 0))
                } else {
                    do0@do {
                        np = ((((sfb.also { sfb = sfb.plus(1) }.value).toUInt()) / 2u)).toInt()
                        pairs_to_decode = (if (big_val_cnt > np) np else big_val_cnt)
                        one = scf.also { scf = scf.plus(1) }.value
                        do1@do {
                            run {
                                var j: Int = 0
                                var w: Int = 5
                                var leaf: Int = ((codebook[((bs_cache shr (32 - w))).toInt()]).toInt())
                                while (leaf < 0) {
                                    bs_cache = bs_cache shl w
                                    bs_sh = bs_sh + w
                                    w = leaf and 7
                                    leaf = (codebook[(((bs_cache shr (32 - w))).toInt()) - (leaf shr 3)]).toInt()
                                }
                                bs_cache = bs_cache shl (leaf shr 8)
                                bs_sh = bs_sh + (leaf shr 8)
                                j = 0
                                while (j < 2) {
                                    run {
                                        var lsb: Int = (leaf and 15)
                                        dst.value = g_pow43[(16 + lsb) - (16 * (((bs_cache shr 31)).toInt()))] * one
                                        bs_cache = bs_cache shl (if ((lsb).toBool()) 1 else 0)
                                        bs_sh = bs_sh + (if ((lsb).toBool()) 1 else 0)

                                    }
                                    run { j++; dst.also { dst = dst.plus(1) }; run { leaf shr 4 }.also { `$` -> leaf = `$` } }
                                }
                                while (bs_sh >= 0) {
                                    bs_cache = bs_cache or (((bs_next_ptr.also { bs_next_ptr = bs_next_ptr.plus(1) }.value).toUInt()) shl bs_sh)
                                    bs_sh = bs_sh - 8
                                }

                            }
                        } while (((--pairs_to_decode)).toBool())
                    } while (((run { big_val_cnt - np }.also { `$` -> big_val_cnt = `$` }) > 0) && ((--sfb_cnt) >= 0))
                }

            }
        }
        np = 1 - big_val_cnt
        while0@while ((1).toBool()) {
            val __oldPos0 = STACK_PTR
            try {
                var codebook_count1: CPointer<UByte> = (CPointer<UByte>((((if ((gr_info.value.count1_table).toBool()) tab33 else tab32)).ptr).toInt()))
                var leaf: Int = ((codebook_count1[((bs_cache shr (32 - 4))).toInt()]).toInt())
                if (!(((leaf and 8)).toBool())) {
                    leaf = (codebook_count1[(leaf shr 3) + ((((bs_cache shl 4) shr (32 - (leaf and 3)))).toInt())]).toInt()
                }
                bs_cache = bs_cache shl (leaf and 7)
                bs_sh = bs_sh + (leaf and 7)
                if (((((bs_next_ptr.minusPtrUByte(bs.value.buf)) * 8) - 24) + bs_sh) > layer3gr_limit) {
                    break@while0
                }
                if (!(((--np)).toBool())) {
                    np = ((((sfb.also { sfb = sfb.plus(1) }.value).toUInt()) / 2u)).toInt()
                    if (!((np).toBool())) {
                        break@while0
                    }
                    one = scf.also { scf = scf.plus(1) }.value
                }
                if (((leaf and (128 shr 0))).toBool()) {
                    dst[0] = (if (((bs_cache).toInt()) < 0) (-one) else one)
                    bs_cache = bs_cache shl 1
                    bs_sh = bs_sh + 1
                }
                if (((leaf and (128 shr 1))).toBool()) {
                    dst[1] = (if (((bs_cache).toInt()) < 0) (-one) else one)
                    bs_cache = bs_cache shl 1
                    bs_sh = bs_sh + 1
                }
                if (!(((--np)).toBool())) {
                    np = ((((sfb.also { sfb = sfb.plus(1) }.value).toUInt()) / 2u)).toInt()
                    if (!((np).toBool())) {
                        break@while0
                    }
                    one = scf.also { scf = scf.plus(1) }.value
                }
                if (((leaf and (128 shr 2))).toBool()) {
                    dst[2] = (if (((bs_cache).toInt()) < 0) (-one) else one)
                    bs_cache = bs_cache shl 1
                    bs_sh = bs_sh + 1
                }
                if (((leaf and (128 shr 3))).toBool()) {
                    dst[3] = (if (((bs_cache).toInt()) < 0) (-one) else one)
                    bs_cache = bs_cache shl 1
                    bs_sh = bs_sh + 1
                }
                while (bs_sh >= 0) {
                    bs_cache = bs_cache or (((bs_next_ptr.also { bs_next_ptr = bs_next_ptr.plus(1) }.value).toUInt()) shl bs_sh)
                    bs_sh = bs_sh - 8
                }

            }
            finally {
                STACK_PTR = __oldPos0
            }
            dst = dst.plus(4)
        }
        bs.value.pos = layer3gr_limit

    }
    fun L3_midside_stereo(left: CPointer<Float>, n: Int): Unit {
        var i: Int = 0
        var right: CPointer<Float> = (left.plus(576))
        while (i < n) {
            run {
                var a: Float = left[i]
                var b: Float = right[i]
                left[i] = a + b
                right[i] = a - b

            }
            i = i + 1
        }

    }
    fun L3_intensity_stereo_band(left: CPointer<Float>, n: Int, kl: Float, kr: Float): Unit {
        var i: Int = 0
        i = 0
        while (i < n) {
            left[i + 576] = left[i] * kr
            left[i] = left[i] * kl
            i = i + 1
        }

    }
    fun L3_stereo_top_band(right: CPointer<Float>, sfb: CPointer<UByte>, nbands: Int, max_band: Array3Int): Unit {
        var right = right // Mutating parameter
        var i: Int = 0
        var k: Int = 0
        max_band[0] = run { run { -1 }.also { `$` -> max_band[2] = `$` } }.also { `$` -> max_band[1] = `$` }
        i = 0
        while0@while (i < nbands) {
            k = 0
            while1@while (k < ((sfb[i]).toInt())) {
                if ((right[k] != 0f) || (right[k + 1] != 0f)) {
                    max_band[i % 3] = i
                    break@while1
                }
                k = k + 2
            }
            right = right.plus(((sfb[i]).toInt()))
            i = i + 1
        }

    }
    fun L3_stereo_process(left: CPointer<Float>, ist_pos: CPointer<UByte>, sfb: CPointer<UByte>, hdr: CPointer<UByte>, max_band: Array3Int, mpeg2_sh: Int): Unit {
        var left = left // Mutating parameter
        var g_pan: Array14Float = __STATIC_L3_stereo_process_g_pan
        var i: UInt = 0u
        var max_pos: UInt = (((if (((((hdr[1]).toUInt()) and 8u)).toBool()) 7 else 64)).toUInt())
        i = 0u
        while ((sfb[(i).toInt()]).toBool()) {
            run {
                var ipos: UInt = ((ist_pos[(i).toInt()]).toUInt())
                if ((((i).toInt()) > max_band[((i % 3u)).toInt()]) && (ipos < max_pos)) {
                    run {
                        var kl: Float = 0f
                        var kr: Float = 0f
                        var s: Float = (if (((((hdr[3]).toUInt()) and 32u)).toBool()) 1.41421356f else 1f)
                        if (((((hdr[1]).toUInt()) and 8u)).toBool()) {
                            kl = g_pan[2 * ((ipos).toInt())]
                            kr = g_pan[(2 * ((ipos).toInt())) + 1]
                        } else {
                            kl = 1f
                            kr = L3_ldexp_q2(1f, (((((ipos).toInt()) + 1) shr 1) shl mpeg2_sh))
                            if (((ipos and 1u)).toBool()) {
                                kl = kr
                                kr = 1f
                            }
                        }
                        L3_intensity_stereo_band(left, ((sfb[(i).toInt()]).toInt()), (kl * s), (kr * s))

                    }
                } else {
                    if (((((hdr[3]).toUInt()) and 32u)).toBool()) {
                        L3_midside_stereo(left, ((sfb[(i).toInt()]).toInt()))
                    }
                }
                left = left.plus(((sfb[(i).toInt()]).toInt()))

            }
            i = i + 1u
        }

    }
    fun L3_intensity_stereo(left: CPointer<Float>, ist_pos: CPointer<UByte>, gr: CPointer<L3_gr_info_t>, hdr: CPointer<UByte>): Unit = stackFrame {
        var max_band: Array3Int = Array3IntAlloc { this[0] = 0 }
        var n_sfb: Int = (((((gr.value.n_long_sfb).toUInt()) + ((gr.value.n_short_sfb).toUInt()))).toInt())
        var i: Int = 0
        var max_blocks: Int = (if ((gr.value.n_short_sfb).toBool()) 3 else 1)
        L3_stereo_top_band((left.plus(576)), gr.value.sfbtab, n_sfb, max_band)
        if ((gr.value.n_long_sfb).toBool()) {
            max_band[0] = run { run { (if ((if (max_band[0] < max_band[1]) max_band[1] else max_band[0]) < max_band[2]) max_band[2] else (if (max_band[0] < max_band[1]) max_band[1] else max_band[0])) }.also { `$` -> max_band[2] = `$` } }.also { `$` -> max_band[1] = `$` }
        }
        i = 0
        while (i < max_blocks) {
            run {
                var default_pos: Int = (if (((((hdr[1]).toUInt()) and 8u)).toBool()) 3 else 0)
                var itop: Int = ((n_sfb - max_blocks) + i)
                var prev: Int = (itop - max_blocks)
                ist_pos[itop] = ((if (max_band[i] >= prev) default_pos else ((ist_pos[prev]).toInt()))).toUByte()

            }
            i = i + 1
        }
        L3_stereo_process(left, ist_pos, gr.value.sfbtab, hdr, max_band, (((((gr[1].scalefac_compress).toUInt()) and 1u)).toInt()))

    }
    fun L3_reorder(grbuf: CPointer<Float>, scratch: CPointer<Float>, sfb: CPointer<UByte>): Unit {
        var sfb = sfb // Mutating parameter
        var i: Int = 0
        var len: Int = 0
        var src: CPointer<Float> = grbuf
        var dst: CPointer<Float> = scratch
        while (0 != (run { (sfb.value).toInt() }.also { `$` -> len = `$` })) {
            i = 0
            while (i < len) {
                dst.also { dst = dst.plus(1) }.value = src[0 * len]
                dst.also { dst = dst.plus(1) }.value = src[1 * len]
                dst.also { dst = dst.plus(1) }.value = src[2 * len]
                run { i++; src.also { src = src.plus(1) } }
            }
            run { run { sfb.plus(3) }.also { `$` -> sfb = `$` }; run { src.plus((2 * len)) }.also { `$` -> src = `$` } }
        }
        memcpy((CPointer<Unit>(((grbuf).ptr).toInt())), (CPointer<Unit>(((scratch).ptr).toInt())), ((dst.minusPtrFloat(scratch)) * Float.SIZE_BYTES))

    }
    fun L3_antialias(grbuf: CPointer<Float>, nbands: Int): Unit {
        var grbuf = grbuf // Mutating parameter
        var nbands = nbands // Mutating parameter
        var g_aa: Array2Array8Float = __STATIC_L3_antialias_g_aa
        while (nbands > 0) {
            run {
                var i: Int = 0
                while (i < 8) {
                    run {
                        var u: Float = grbuf[18 + i]
                        var d: Float = grbuf[17 - i]
                        grbuf[18 + i] = (u * g_aa[0][i]) - (d * g_aa[1][i])
                        grbuf[17 - i] = (u * g_aa[1][i]) + (d * g_aa[0][i])

                    }
                    i = i + 1
                }

            }
            run { nbands--; run { grbuf.plus(18) }.also { `$` -> grbuf = `$` } }
        }

    }
    fun L3_dct3_9(y: CPointer<Float>): Unit {
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
    fun L3_imdct36(grbuf: CPointer<Float>, overlap: CPointer<Float>, window: CPointer<Float>, nbands: Int): Unit {
        var grbuf = grbuf // Mutating parameter
        var overlap = overlap // Mutating parameter
        var i: Int = 0
        var j: Int = 0
        var g_twid9: Array18Float = __STATIC_L3_imdct36_g_twid9
        j = 0
        while (j < nbands) {
            stackFrame {
                var co: Array9Float = Array9FloatAlloc { this[0] = 0f }
                var si: Array9Float = Array9FloatAlloc { this[0] = 0f }
                co[0] = -grbuf[0]
                si[0] = grbuf[17]
                i = 0
                while (i < 4) {
                    si[8 - (2 * i)] = grbuf[(4 * i) + 1] - grbuf[(4 * i) + 2]
                    co[1 + (2 * i)] = grbuf[(4 * i) + 1] + grbuf[(4 * i) + 2]
                    si[7 - (2 * i)] = grbuf[(4 * i) + 4] - grbuf[(4 * i) + 3]
                    co[2 + (2 * i)] = -(grbuf[(4 * i) + 3] + grbuf[(4 * i) + 4])
                    i = i + 1
                }
                L3_dct3_9((CPointer<Float>(((co).ptr).toInt())))
                L3_dct3_9((CPointer<Float>(((si).ptr).toInt())))
                si[1] = -si[1]
                si[3] = -si[3]
                si[5] = -si[5]
                si[7] = -si[7]
                i = 0
                while (i < 9) {
                    run {
                        var ovl: Float = overlap[i]
                        var sum: Float = ((co[i] * g_twid9[9 + i]) + (si[i] * g_twid9[0 + i]))
                        overlap[i] = (co[i] * g_twid9[0 + i]) - (si[i] * g_twid9[9 + i])
                        grbuf[i] = (ovl * window[0 + i]) - (sum * window[9 + i])
                        grbuf[17 - i] = (ovl * window[9 + i]) + (sum * window[0 + i])

                    }
                    i = i + 1
                }

            }
            run { j++; run { grbuf.plus(18) }.also { `$` -> grbuf = `$` }; run { overlap.plus(9) }.also { `$` -> overlap = `$` } }
        }

    }
    fun L3_idct3(x0: Float, x1: Float, x2: Float, dst: CPointer<Float>): Unit {
        var m1: Float = (x1 * 0.8660254f)
        var a1: Float = (x0 - (x2 * 0.5f))
        dst[1] = x0 + x2
        dst[0] = a1 + m1
        dst[2] = a1 - m1

    }
    fun L3_imdct12(x: CPointer<Float>, dst: CPointer<Float>, overlap: CPointer<Float>): Unit = stackFrame {
        var g_twid3: Array6Float = __STATIC_L3_imdct12_g_twid3
        var co: Array3Float = Array3FloatAlloc { this[0] = 0f }
        var si: Array3Float = Array3FloatAlloc { this[0] = 0f }
        var i: Int = 0
        L3_idct3((-x[0]), (x[6] + x[3]), (x[12] + x[9]), (CPointer<Float>(((co).ptr).toInt())))
        L3_idct3(x[15], (x[12] - x[9]), (x[6] - x[3]), (CPointer<Float>(((si).ptr).toInt())))
        si[1] = -si[1]
        i = 0
        while (i < 3) {
            run {
                var ovl: Float = overlap[i]
                var sum: Float = ((co[i] * g_twid3[3 + i]) + (si[i] * g_twid3[0 + i]))
                overlap[i] = (co[i] * g_twid3[0 + i]) - (si[i] * g_twid3[3 + i])
                dst[i] = (ovl * g_twid3[2 - i]) - (sum * g_twid3[5 - i])
                dst[5 - i] = (ovl * g_twid3[5 - i]) + (sum * g_twid3[2 - i])

            }
            i = i + 1
        }

    }
    fun L3_imdct_short(grbuf: CPointer<Float>, overlap: CPointer<Float>, nbands: Int): Unit {
        var grbuf = grbuf // Mutating parameter
        var overlap = overlap // Mutating parameter
        var nbands = nbands // Mutating parameter
        while (nbands > 0) {
            stackFrame {
                var tmp: Array18Float = Array18FloatAlloc { this[0] = 0f }
                memcpy((CPointer<Unit>(((tmp).ptr).toInt())), (CPointer<Unit>(((grbuf).ptr).toInt())), 72)
                memcpy((CPointer<Unit>(((grbuf).ptr).toInt())), (CPointer<Unit>(((overlap).ptr).toInt())), (6 * Float.SIZE_BYTES))
                L3_imdct12((CPointer<Float>(((tmp).ptr).toInt())), (grbuf.plus(6)), (overlap.plus(6)))
                L3_imdct12((tmp.plus(1)), (grbuf.plus(12)), (overlap.plus(6)))
                L3_imdct12((tmp.plus(2)), overlap, (overlap.plus(6)))

            }
            run { nbands--; run { overlap.plus(9) }.also { `$` -> overlap = `$` }; run { grbuf.plus(18) }.also { `$` -> grbuf = `$` } }
        }

    }
    fun L3_change_sign(grbuf: CPointer<Float>): Unit {
        var grbuf = grbuf // Mutating parameter
        var b: Int = 0
        var i: Int = 0
        run { run { 0 }.also { `$` -> b = `$` }; run { grbuf.plus(18) }.also { `$` -> grbuf = `$` } }
        while (b < 32) {
            i = 1
            while (i < 18) {
                grbuf[i] = -grbuf[i]
                i = i + 2
            }
            run { run { b + 2 }.also { `$` -> b = `$` }; run { grbuf.plus(36) }.also { `$` -> grbuf = `$` } }
        }

    }
    fun L3_imdct_gr(grbuf: CPointer<Float>, overlap: CPointer<Float>, block_type: UInt, n_long_bands: UInt): Unit {
        var grbuf = grbuf // Mutating parameter
        var overlap = overlap // Mutating parameter
        var g_mdct_window: Array2Array18Float = __STATIC_L3_imdct_gr_g_mdct_window
        if ((n_long_bands).toBool()) {
            L3_imdct36(grbuf, overlap, (CPointer<Float>(((g_mdct_window[0]).ptr).toInt())), ((n_long_bands).toInt()))
            grbuf = grbuf.plus((18 * ((n_long_bands).toInt())))
            overlap = overlap.plus((9 * ((n_long_bands).toInt())))
        }
        if (((block_type).toInt()) == 2) {
            L3_imdct_short(grbuf, overlap, (32 - ((n_long_bands).toInt())))
        } else {
            L3_imdct36(grbuf, overlap, (CPointer<Float>(((g_mdct_window[((((block_type).toInt()) == 3)).toInt().toInt()]).ptr).toInt())), (32 - ((n_long_bands).toInt())))
        }

    }
    fun L3_save_reservoir(h: CPointer<mp3dec_t>, s: CPointer<mp3dec_scratch_t>): Unit {
        var pos: Int = ((s.value.bs.pos + 7) / 8)
        var remains: Int = ((s.value.bs.limit / 8) - pos)
        if (remains > 511) {
            pos = pos + (remains - 511)
            remains = 511
        }
        if (remains > 0) {
            memmove((CPointer<Unit>(((h.value.reserv_buf).ptr).toInt())), (CPointer<Unit>((((s.value.maindata.plus(pos))).ptr).toInt())), remains)
        }
        h.value.reserv = remains

    }
    fun L3_restore_reservoir(h: CPointer<mp3dec_t>, bs: CPointer<bs_t>, s: CPointer<mp3dec_scratch_t>, main_data_begin: Int): Int {
        var frame_bytes: Int = ((bs.value.limit - bs.value.pos) / 8)
        var bytes_have: Int = (if (h.value.reserv > main_data_begin) main_data_begin else h.value.reserv)
        memcpy((CPointer<Unit>(((s.value.maindata).ptr).toInt())), (CPointer<Unit>((((h.value.reserv_buf.plus((if (0 < (h.value.reserv - main_data_begin)) (h.value.reserv - main_data_begin) else 0)))).ptr).toInt())), (if (h.value.reserv > main_data_begin) main_data_begin else h.value.reserv))
        memcpy((CPointer<Unit>((((s.value.maindata.plus(bytes_have))).ptr).toInt())), (CPointer<Unit>((((bs.value.buf.plus((bs.value.pos / 8)))).ptr).toInt())), frame_bytes)
        bs_init((CPointer((s).ptr + mp3dec_scratch_t.OFFSET_bs)), (CPointer<UByte>(((s.value.maindata).ptr).toInt())), (bytes_have + frame_bytes))
        return ((h.value.reserv >= main_data_begin)).toInt().toInt()

    }
    fun L3_decode(h: CPointer<mp3dec_t>, s: CPointer<mp3dec_scratch_t>, gr_info: CPointer<L3_gr_info_t>, nch: Int): Unit {
        var gr_info = gr_info // Mutating parameter
        var ch: Int = 0
        ch = 0
        while (ch < nch) {
            run {
                var layer3gr_limit: Int = (s.value.bs.pos + ((gr_info[ch].part_23_length).toInt()))
                L3_decode_scalefactors((CPointer<UByte>(((h.value.header).ptr).toInt())), (CPointer<UByte>(((s.value.ist_pos[ch]).ptr).toInt())), (CPointer((s).ptr + mp3dec_scratch_t.OFFSET_bs)), (gr_info.plus(ch)), (CPointer<Float>(((s.value.scf).ptr).toInt())), ch)
                L3_huffman((CPointer<Float>(((s.value.grbuf[ch]).ptr).toInt())), (CPointer((s).ptr + mp3dec_scratch_t.OFFSET_bs)), (gr_info.plus(ch)), (CPointer<Float>(((s.value.scf).ptr).toInt())), layer3gr_limit)

            }
            ch = ch + 1
        }
        if (((((h.value.header[3]).toUInt()) and 16u)).toBool()) {
            L3_intensity_stereo((CPointer<Float>(((s.value.grbuf[0]).ptr).toInt())), (CPointer<UByte>(((s.value.ist_pos[1]).ptr).toInt())), gr_info, (CPointer<UByte>(((h.value.header).ptr).toInt())))
        } else {
            if ((((((h.value.header[3]).toUInt()) and 224u)).toInt()) == 96) {
                L3_midside_stereo((CPointer<Float>(((s.value.grbuf[0]).ptr).toInt())), 576)
            }
        }
        ch = 0
        while (ch < nch) {
            run {
                var aa_bands: Int = 31
                var n_long_bands: Int = ((if ((gr_info.value.mixed_block_flag).toBool()) 2 else 0) shl ((((((((((h.value.header[2]).toUInt()) shr 2) and 3u) + ((((((h.value.header[1]).toUInt()) shr 3) and 1u) + ((((h.value.header[1]).toUInt()) shr 4) and 1u)) * 3u))).toInt()) == 2)).toInt().toInt()))
                if ((gr_info.value.n_short_sfb).toBool()) {
                    aa_bands = n_long_bands - 1
                    L3_reorder((s.value.grbuf[ch].plus((n_long_bands * 18))), (CPointer<Float>(((s.value.syn[0]).ptr).toInt())), (gr_info.value.sfbtab.plus(((gr_info.value.n_long_sfb).toInt()))))
                }
                L3_antialias((CPointer<Float>(((s.value.grbuf[ch]).ptr).toInt())), aa_bands)
                L3_imdct_gr((CPointer<Float>(((s.value.grbuf[ch]).ptr).toInt())), (CPointer<Float>(((h.value.mdct_overlap[ch]).ptr).toInt())), ((gr_info.value.block_type).toUInt()), ((n_long_bands).toUInt()))
                L3_change_sign((CPointer<Float>(((s.value.grbuf[ch]).ptr).toInt())))

            }
            run { ch++; gr_info.also { gr_info = gr_info.plus(1) } }
        }

    }
    fun mp3d_DCT_II(grbuf: CPointer<Float>, n: Int): Unit {
        var g_sec: Array24Float = __STATIC_mp3d_DCT_II_g_sec
        var i: Int = 0
        var k: Int = 0
        while (k < n) {
            stackFrame {
                var t: Array4Array8Float = Array4Array8FloatAlloc { this[0] = (Array8Float((0))) }
                var x: CPointer<Float> = CPointer(0)
                var y: CPointer<Float> = (grbuf.plus(k))
                run { run { CPointer<Float>(((t[0]).ptr).toInt()) }.also { `$` -> x = `$` }; run { 0 }.also { `$` -> i = `$` } }
                while (i < 8) {
                    run {
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
                    run { i++; x.also { x = x.plus(1) } }
                }
                run { run { CPointer<Float>(((t[0]).ptr).toInt()) }.also { `$` -> x = `$` }; run { 0 }.also { `$` -> i = `$` } }
                while (i < 4) {
                    run {
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
                    run { i++; run { x.plus(8) }.also { `$` -> x = `$` } }
                }
                i = 0
                while (i < 7) {
                    y[0 * 18] = t[0][i]
                    y[1 * 18] = (t[2][i] + t[3][i]) + t[3][i + 1]
                    y[2 * 18] = t[1][i] + t[1][i + 1]
                    y[3 * 18] = (t[2][i + 1] + t[3][i]) + t[3][i + 1]
                    run { i++; run { y.plus((4 * 18)) }.also { `$` -> y = `$` } }
                }
                y[0 * 18] = t[0][7]
                y[1 * 18] = t[2][7] + t[3][7]
                y[2 * 18] = t[1][7]
                y[3 * 18] = t[3][7]

            }
            k = k + 1
        }

    }
    fun mp3d_scale_pcm(sample: Float): Short {
        if (((sample).toDouble()) >= 32766.5) {
            return (32767).toShort()
        }
        if (((sample).toDouble()) <= -32767.5) {
            return (-32768L).toShort()
        }
        var s: Short = (((sample + 0.5f)).toShort())
        s = ((((s).toInt()) - (((s < ((0).toShort()))).toInt().toInt()))).toShort()
        return s

    }
    fun mp3d_synth_pair(pcm: CPointer<Short>, nch: Int, z: CPointer<Float>): Unit {
        var z = z // Mutating parameter
        var a: Float = 0f
        a = (z[14 * 64] - z[0]) * 29f
        a = a + ((z[1 * 64] + z[13 * 64]) * 213f)
        a = a + ((z[12 * 64] - z[2 * 64]) * 459f)
        a = a + ((z[3 * 64] + z[11 * 64]) * 2037f)
        a = a + ((z[10 * 64] - z[4 * 64]) * 5153f)
        a = a + ((z[5 * 64] + z[9 * 64]) * 6574f)
        a = a + ((z[8 * 64] - z[6 * 64]) * 37489f)
        a = a + (z[7 * 64] * 75038f)
        pcm[0] = mp3d_scale_pcm(a)
        z = z.plus(2)
        a = z[14 * 64] * 104f
        a = a + (z[12 * 64] * 1567f)
        a = a + (z[10 * 64] * 9727f)
        a = a + (z[8 * 64] * 64019f)
        a = a + (z[6 * 64] * -9975f)
        a = a + (z[4 * 64] * -45f)
        a = a + (z[2 * 64] * 146f)
        a = a + (z[0 * 64] * -5f)
        pcm[16 * nch] = mp3d_scale_pcm(a)

    }
    fun mp3d_synth(xl: CPointer<Float>, dstl: CPointer<Short>, nch: Int, lins: CPointer<Float>): Unit {
        var i: Int = 0
        var xr: CPointer<Float> = (xl.plus((576 * (nch - 1))))
        var dstr: CPointer<Short> = (dstl.plus((nch - 1)))
        var g_win: CPointer<Float> = __STATIC_mp3d_synth_g_win
        var zlin: CPointer<Float> = (lins.plus((15 * 64)))
        var w: CPointer<Float> = (CPointer<Float>(((g_win).ptr).toInt()))
        zlin[4 * 15] = xl[18 * 16]
        zlin[(4 * 15) + 1] = xr[18 * 16]
        zlin[(4 * 15) + 2] = xl[0]
        zlin[(4 * 15) + 3] = xr[0]
        zlin[4 * 31] = xl[1 + (18 * 16)]
        zlin[(4 * 31) + 1] = xr[1 + (18 * 16)]
        zlin[(4 * 31) + 2] = xl[1]
        zlin[(4 * 31) + 3] = xr[1]
        mp3d_synth_pair(dstr, nch, ((lins.plus((4 * 15))).plus(1)))
        mp3d_synth_pair((dstr.plus((32 * nch))), nch, (((lins.plus((4 * 15))).plus(64)).plus(1)))
        mp3d_synth_pair(dstl, nch, (lins.plus((4 * 15))))
        mp3d_synth_pair((dstl.plus((32 * nch))), nch, ((lins.plus((4 * 15))).plus(64)))
        i = 14
        while (i >= 0) {
            stackFrame {
                var a: Array4Float = Array4FloatAlloc { this[0] = 0f }
                var b: Array4Float = Array4FloatAlloc { this[0] = 0f }
                zlin[4 * i] = xl[18 * (31 - i)]
                zlin[(4 * i) + 1] = xr[18 * (31 - i)]
                zlin[(4 * i) + 2] = xl[1 + (18 * (31 - i))]
                zlin[(4 * i) + 3] = xr[1 + (18 * (31 - i))]
                zlin[4 * (i + 16)] = xl[1 + (18 * (1 + i))]
                zlin[(4 * (i + 16)) + 1] = xr[1 + (18 * (1 + i))]
                zlin[(4 * (i - 16)) + 2] = xl[18 * (1 + i)]
                zlin[(4 * (i - 16)) + 3] = xr[18 * (1 + i)]
                run {
                    var j: Int = 0
                    var w0: Float = w.also { w = w.plus(1) }.value
                    var w1: Float = w.also { w = w.plus(1) }.value
                    var vz: CPointer<Float> = (((zlin).plus((4 * i) - (0 * 64))))
                    var vy: CPointer<Float> = (((zlin).plus((4 * i) - ((15 - 0) * 64))))
                    j = 0
                    while (j < 4) {
                        run { run { (vz[j] * w1) + (vy[j] * w0) }.also { `$` -> b[j] = `$` }; run { (vz[j] * w0) - (vy[j] * w1) }.also { `$` -> a[j] = `$` } }
                        j = j + 1
                    }

                }
                run {
                    var j: Int = 0
                    var w0: Float = w.also { w = w.plus(1) }.value
                    var w1: Float = w.also { w = w.plus(1) }.value
                    var vz: CPointer<Float> = (((zlin).plus((4 * i) - (1 * 64))))
                    var vy: CPointer<Float> = (((zlin).plus((4 * i) - ((15 - 1) * 64))))
                    j = 0
                    while (j < 4) {
                        run { run { b[j] + ((vz[j] * w1) + (vy[j] * w0)) }.also { `$` -> b[j] = `$` }; run { a[j] + ((vy[j] * w1) - (vz[j] * w0)) }.also { `$` -> a[j] = `$` } }
                        j = j + 1
                    }

                }
                run {
                    var j: Int = 0
                    var w0: Float = w.also { w = w.plus(1) }.value
                    var w1: Float = w.also { w = w.plus(1) }.value
                    var vz: CPointer<Float> = (((zlin).plus((4 * i) - (2 * 64))))
                    var vy: CPointer<Float> = (((zlin).plus((4 * i) - ((15 - 2) * 64))))
                    j = 0
                    while (j < 4) {
                        run { run { b[j] + ((vz[j] * w1) + (vy[j] * w0)) }.also { `$` -> b[j] = `$` }; run { a[j] + ((vz[j] * w0) - (vy[j] * w1)) }.also { `$` -> a[j] = `$` } }
                        j = j + 1
                    }

                }
                run {
                    var j: Int = 0
                    var w0: Float = w.also { w = w.plus(1) }.value
                    var w1: Float = w.also { w = w.plus(1) }.value
                    var vz: CPointer<Float> = (((zlin).plus((4 * i) - (3 * 64))))
                    var vy: CPointer<Float> = (((zlin).plus((4 * i) - ((15 - 3) * 64))))
                    j = 0
                    while (j < 4) {
                        run { run { b[j] + ((vz[j] * w1) + (vy[j] * w0)) }.also { `$` -> b[j] = `$` }; run { a[j] + ((vy[j] * w1) - (vz[j] * w0)) }.also { `$` -> a[j] = `$` } }
                        j = j + 1
                    }

                }
                run {
                    var j: Int = 0
                    var w0: Float = w.also { w = w.plus(1) }.value
                    var w1: Float = w.also { w = w.plus(1) }.value
                    var vz: CPointer<Float> = (((zlin).plus((4 * i) - (4 * 64))))
                    var vy: CPointer<Float> = (((zlin).plus((4 * i) - ((15 - 4) * 64))))
                    j = 0
                    while (j < 4) {
                        run { run { b[j] + ((vz[j] * w1) + (vy[j] * w0)) }.also { `$` -> b[j] = `$` }; run { a[j] + ((vz[j] * w0) - (vy[j] * w1)) }.also { `$` -> a[j] = `$` } }
                        j = j + 1
                    }

                }
                run {
                    var j: Int = 0
                    var w0: Float = w.also { w = w.plus(1) }.value
                    var w1: Float = w.also { w = w.plus(1) }.value
                    var vz: CPointer<Float> = (((zlin).plus((4 * i) - (5 * 64))))
                    var vy: CPointer<Float> = (((zlin).plus((4 * i) - ((15 - 5) * 64))))
                    j = 0
                    while (j < 4) {
                        run { run { b[j] + ((vz[j] * w1) + (vy[j] * w0)) }.also { `$` -> b[j] = `$` }; run { a[j] + ((vy[j] * w1) - (vz[j] * w0)) }.also { `$` -> a[j] = `$` } }
                        j = j + 1
                    }

                }
                run {
                    var j: Int = 0
                    var w0: Float = w.also { w = w.plus(1) }.value
                    var w1: Float = w.also { w = w.plus(1) }.value
                    var vz: CPointer<Float> = (((zlin).plus((4 * i) - (6 * 64))))
                    var vy: CPointer<Float> = (((zlin).plus((4 * i) - ((15 - 6) * 64))))
                    j = 0
                    while (j < 4) {
                        run { run { b[j] + ((vz[j] * w1) + (vy[j] * w0)) }.also { `$` -> b[j] = `$` }; run { a[j] + ((vz[j] * w0) - (vy[j] * w1)) }.also { `$` -> a[j] = `$` } }
                        j = j + 1
                    }

                }
                run {
                    var j: Int = 0
                    var w0: Float = w.also { w = w.plus(1) }.value
                    var w1: Float = w.also { w = w.plus(1) }.value
                    var vz: CPointer<Float> = (((zlin).plus((4 * i) - (7 * 64))))
                    var vy: CPointer<Float> = (((zlin).plus((4 * i) - ((15 - 7) * 64))))
                    j = 0
                    while (j < 4) {
                        run { run { b[j] + ((vz[j] * w1) + (vy[j] * w0)) }.also { `$` -> b[j] = `$` }; run { a[j] + ((vy[j] * w1) - (vz[j] * w0)) }.also { `$` -> a[j] = `$` } }
                        j = j + 1
                    }

                }
                dstr[(15 - i) * nch] = mp3d_scale_pcm(a[1])
                dstr[(17 + i) * nch] = mp3d_scale_pcm(b[1])
                dstl[(15 - i) * nch] = mp3d_scale_pcm(a[0])
                dstl[(17 + i) * nch] = mp3d_scale_pcm(b[0])
                dstr[(47 - i) * nch] = mp3d_scale_pcm(a[3])
                dstr[(49 + i) * nch] = mp3d_scale_pcm(b[3])
                dstl[(47 - i) * nch] = mp3d_scale_pcm(a[2])
                dstl[(49 + i) * nch] = mp3d_scale_pcm(b[2])

            }
            i = i - 1
        }

    }
    fun mp3d_synth_granule(qmf_state: CPointer<Float>, grbuf: CPointer<Float>, nbands: Int, nch: Int, pcm: CPointer<Short>, lins: CPointer<Float>): Unit {
        var i: Int = 0
        i = 0
        while (i < nch) {
            mp3d_DCT_II((grbuf.plus((576 * i))), nbands)
            i = i + 1
        }
        memcpy((CPointer<Unit>(((lins).ptr).toInt())), (CPointer<Unit>(((qmf_state).ptr).toInt())), ((Float.SIZE_BYTES * 15) * 64))
        i = 0
        while (i < nbands) {
            mp3d_synth((grbuf.plus(i)), (pcm.plus((32 * (nch * i)))), nch, (lins.plus((i * 64))))
            i = i + 2
        }
        if (nch == 1) {
            i = 0
            while (i < (15 * 64)) {
                qmf_state[i] = lins[(nbands * 64) + i]
                i = i + 2
            }
        } else {
            memcpy((CPointer<Unit>(((qmf_state).ptr).toInt())), (CPointer<Unit>((((lins.plus((nbands * 64)))).ptr).toInt())), ((Float.SIZE_BYTES * 15) * 64))
        }

    }
    fun mp3d_match_frame(hdr: CPointer<UByte>, mp3_bytes: Int, frame_bytes: Int): Int {
        var i: Int = 0
        var nmatch: Int = 0
        run { run { 0 }.also { `$` -> i = `$` }; run { 0 }.also { `$` -> nmatch = `$` } }
        while (nmatch < 10) {
            i = i + (hdr_frame_bytes((hdr.plus(i)), frame_bytes) + hdr_padding((hdr.plus(i))))
            if ((i + 4) > mp3_bytes) {
                return ((nmatch > 0)).toInt().toInt()
            }
            if (!((hdr_compare(hdr, (hdr.plus(i)))).toBool())) {
                return 0
            }
            nmatch = nmatch + 1
        }
        return 1

    }
    fun mp3d_find_frame(mp3: CPointer<UByte>, mp3_bytes: Int, free_format_bytes: CPointer<Int>, ptr_frame_bytes: CPointer<Int>): Int {
        var mp3 = mp3 // Mutating parameter
        var i: Int = 0
        var k: Int = 0
        i = 0
        while0@while (i < (mp3_bytes - 4)) {
            if ((hdr_valid(mp3)).toBool()) {
                val __oldPos2 = STACK_PTR
                try {
                    var frame_bytes: Int = hdr_frame_bytes(mp3, free_format_bytes.value)
                    var frame_and_padding: Int = (frame_bytes + hdr_padding(mp3))
                    k = 4
                    while1@while (((!((frame_bytes).toBool())) && (k < 2304)) && (((i + (2 * (((k < (mp3_bytes - 4))).toInt().toInt())))).toBool())) {
                        if ((hdr_compare(mp3, (mp3.plus(k)))).toBool()) {
                            val __oldPos1 = STACK_PTR
                            try {
                                var fb: Int = (k - hdr_padding(mp3))
                                var nextfb: Int = (fb + hdr_padding((mp3.plus(k))))
                                if (((((i + k) + nextfb) + 4) > mp3_bytes) || (!((hdr_compare(mp3, ((mp3.plus(k)).plus(nextfb)))).toBool()))) {
                                    k = k + 1
                                    continue@while1
                                }
                                frame_and_padding = k
                                frame_bytes = fb
                                free_format_bytes.value = fb

                            }
                            finally {
                                STACK_PTR = __oldPos1
                            }
                        }
                        k = k + 1
                    }
                    if (((((frame_bytes).toBool()) && (((i + (((frame_and_padding <= mp3_bytes)).toInt().toInt()))).toBool())) && ((mp3d_match_frame(mp3, (mp3_bytes - i), frame_bytes)).toBool())) || ((!((i).toBool())) && (frame_and_padding == mp3_bytes))) {
                        ptr_frame_bytes.value = frame_and_padding
                        return i
                    }
                    free_format_bytes.value = 0

                }
                finally {
                    STACK_PTR = __oldPos2
                }
            }
            run { i++; mp3.also { mp3 = mp3.plus(1) } }
        }
        ptr_frame_bytes.value = 0
        return i

    }
    fun mp3dec_init(dec: CPointer<mp3dec_t>): Unit {
        dec.value.header[0] = (0).toUByte()

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
        var bs_frame: Array1bs_t = Array1bs_tAlloc { this[0] = (bs_t((0))) }
        var scratch: mp3dec_scratch_t = mp3dec_scratch_tAlloc().copyFrom(mp3dec_scratch_tAlloc())
        if (((mp3_bytes > 4) && (((dec.value.header[0]).toInt()) == 255)) && ((hdr_compare((CPointer<UByte>(((dec.value.header).ptr).toInt())), mp3)).toBool())) {
            frame_size.value = hdr_frame_bytes(mp3, dec.value.free_format_bytes) + hdr_padding(mp3)
            if ((frame_size.value != mp3_bytes) && (((frame_size.value + 4) > mp3_bytes) || (!((hdr_compare(mp3, (mp3.plus(frame_size.value)))).toBool())))) {
                frame_size.value = 0
            }
        }
        if (!((frame_size.value).toBool())) {
            memset((CPointer<Unit>(((dec).ptr).toInt())), 0, mp3dec_t.SIZE_BYTES)
            i = mp3d_find_frame(mp3, mp3_bytes, (CPointer((dec).ptr + mp3dec_t.OFFSET_free_format_bytes)), (CPointer<Int>((frame_size).ptr)))
            if ((!((frame_size.value).toBool())) || (((i + (((frame_size.value > mp3_bytes)).toInt().toInt()))).toBool())) {
                info.value.frame_bytes = i
                return 0
            }
        }
        hdr = mp3.plus(i)
        memcpy((CPointer<Unit>(((dec.value.header).ptr).toInt())), (CPointer<Unit>(((hdr).ptr).toInt())), 4)
        info.value.frame_bytes = i + frame_size.value
        info.value.channels = (if ((((((hdr[3]).toUInt()) and 192u)).toInt()) == 192) 1 else 2)
        info.value.hz = (hdr_sample_rate_hz(hdr)).toInt()
        info.value.layer = 4 - ((((((hdr[1]).toUInt()) shr 1) and 3u)).toInt())
        info.value.bitrate_kbps = (hdr_bitrate_kbps(hdr)).toInt()
        if (!((pcm).toBool())) {
            return (hdr_frame_samples(hdr)).toInt()
        }
        bs_init((CPointer<bs_t>(((bs_frame).ptr).toInt())), (hdr.plus(4)), (frame_size.value - 4))
        if (!(((((hdr[1]).toUInt()) and 1u)).toBool())) {
            get_bits((CPointer<bs_t>(((bs_frame).ptr).toInt())), 16)
        }
        if (info.value.layer == 3) {
            run {
                var main_data_begin: Int = L3_read_side_info((CPointer<bs_t>(((bs_frame).ptr).toInt())), (CPointer<L3_gr_info_t>(((scratch.gr_info).ptr).toInt())), hdr)
                if ((main_data_begin < 0) || (bs_frame.value.pos > bs_frame.value.limit)) {
                    mp3dec_init(dec)
                    return 0
                }
                success = L3_restore_reservoir(dec, (CPointer<bs_t>(((bs_frame).ptr).toInt())), (CPointer<mp3dec_scratch_t>((scratch).ptr)), main_data_begin)
                if ((success).toBool()) {
                    igr = 0
                    while (igr < (if (((((hdr[1]).toUInt()) and 8u)).toBool()) 2 else 1)) {
                        memset((CPointer<Unit>(((scratch.grbuf[0]).ptr).toInt())), 0, ((576 * 2) * Float.SIZE_BYTES))
                        L3_decode(dec, (CPointer<mp3dec_scratch_t>((scratch).ptr)), (scratch.gr_info.plus((igr * info.value.channels))), info.value.channels)
                        mp3d_synth_granule((CPointer<Float>(((dec.value.qmf_state).ptr).toInt())), (CPointer<Float>(((scratch.grbuf[0]).ptr).toInt())), 18, info.value.channels, pcm, (CPointer<Float>(((scratch.syn[0]).ptr).toInt())))
                        run { igr++; run { pcm.plus((576 * info.value.channels)) }.also { `$` -> pcm = `$` } }
                    }
                }
                L3_save_reservoir(dec, (CPointer<mp3dec_scratch_t>((scratch).ptr)))

            }
        } else {
            stackFrame {
                var sci: Array1L12_scale_info = Array1L12_scale_infoAlloc { this[0] = (L12_scale_info((0))) }
                L12_read_scale_info(hdr, (CPointer<bs_t>(((bs_frame).ptr).toInt())), (CPointer<L12_scale_info>(((sci).ptr).toInt())))
                memset((CPointer<Unit>(((scratch.grbuf[0]).ptr).toInt())), 0, ((576 * 2) * Float.SIZE_BYTES))
                run { run { 0 }.also { `$` -> i = `$` }; run { 0 }.also { `$` -> igr = `$` } }
                while (igr < 3) {
                    if (12 == (run { i + L12_dequantize_granule((scratch.grbuf[0].plus(i)), (CPointer<bs_t>(((bs_frame).ptr).toInt())), (CPointer<L12_scale_info>(((sci).ptr).toInt())), (info.value.layer or 1)) }.also { `$` -> i = `$` })) {
                        i = 0
                        L12_apply_scf_384((CPointer<L12_scale_info>(((sci).ptr).toInt())), (sci.value.scf.plus(igr)), (CPointer<Float>(((scratch.grbuf[0]).ptr).toInt())))
                        mp3d_synth_granule((CPointer<Float>(((dec.value.qmf_state).ptr).toInt())), (CPointer<Float>(((scratch.grbuf[0]).ptr).toInt())), 12, info.value.channels, pcm, (CPointer<Float>(((scratch.syn[0]).ptr).toInt())))
                        memset((CPointer<Unit>(((scratch.grbuf[0]).ptr).toInt())), 0, ((576 * 2) * Float.SIZE_BYTES))
                        pcm = pcm.plus((384 * info.value.channels))
                    }
                    if (bs_frame.value.pos > bs_frame.value.limit) {
                        mp3dec_init(dec)
                        return 0
                    }
                    igr = igr + 1
                }

            }
        }
        return success * ((hdr_frame_samples((CPointer<UByte>(((dec.value.header).ptr).toInt())))).toInt())

    }
    fun strlen(str: CPointer<Byte>): Int {
        var str = str // Mutating parameter
        var out: Int = 0
        while (((str.also { str = str.plus(1) }.value).toInt()) != 0) {
            out = out + 1
        }
        return out

    }
    fun memcmp(ptr1: CPointer<Unit>, ptr2: CPointer<Unit>, num: Int): Int {
        var a: CPointer<Byte> = (CPointer<Byte>(((ptr1).ptr).toInt()))
        var b: CPointer<Byte> = (CPointer<Byte>(((ptr2).ptr).toInt()))
        run {
            var n: Int = 0
            while (n < num) {
                run {
                    var res: Int = (((a[n]).toInt()) - ((b[n]).toInt()))
                    if (res < 0) {
                        return -1
                    }
                    if (res > 0) {
                        return 1
                    }

                }
                n = n + 1
            }

        }
        return 0

    }

    //////////////////
    // C STRUCTURES //
    //////////////////

    fun mp3dec_frame_info_tAlloc(): mp3dec_frame_info_t = mp3dec_frame_info_t(alloca(mp3dec_frame_info_t.SIZE_BYTES).ptr)
    fun mp3dec_frame_info_tAlloc(frame_bytes: Int, channels: Int, hz: Int, layer: Int, bitrate_kbps: Int): mp3dec_frame_info_t = mp3dec_frame_info_tAlloc().apply { this.frame_bytes = frame_bytes; this.channels = channels; this.hz = hz; this.layer = layer; this.bitrate_kbps = bitrate_kbps }
    fun mp3dec_frame_info_t.copyFrom(src: mp3dec_frame_info_t): mp3dec_frame_info_t = this.apply { memcpy(CPointer<Unit>(this.ptr), CPointer<Unit>(src.ptr), mp3dec_frame_info_t.SIZE_BYTES) }
    inline fun fixedArrayOfmp3dec_frame_info_t(size: Int, setItems: CPointer<mp3dec_frame_info_t>.() -> Unit): CPointer<mp3dec_frame_info_t> = alloca_zero(size * mp3dec_frame_info_t.SIZE_BYTES).toCPointer<mp3dec_frame_info_t>().apply(setItems)
    @kotlin.jvm.JvmName("getmp3dec_frame_info_t") operator fun CPointer<mp3dec_frame_info_t>.get(index: Int): mp3dec_frame_info_t = mp3dec_frame_info_t(this.ptr + index * mp3dec_frame_info_t.SIZE_BYTES)
    operator fun CPointer<mp3dec_frame_info_t>.set(index: Int, value: mp3dec_frame_info_t) = mp3dec_frame_info_t(this.ptr + index * mp3dec_frame_info_t.SIZE_BYTES).copyFrom(value)
    @kotlin.jvm.JvmName("plusmp3dec_frame_info_t") operator fun CPointer<mp3dec_frame_info_t>.plus(offset: Int): CPointer<mp3dec_frame_info_t> = CPointer(this.ptr + offset * mp3dec_frame_info_t.SIZE_BYTES)
    @kotlin.jvm.JvmName("minusmp3dec_frame_info_t") operator fun CPointer<mp3dec_frame_info_t>.minus(offset: Int): CPointer<mp3dec_frame_info_t> = CPointer(this.ptr - offset * mp3dec_frame_info_t.SIZE_BYTES)
    fun CPointer<mp3dec_frame_info_t>.minusPtrmp3dec_frame_info_t(other: CPointer<mp3dec_frame_info_t>) = (this.ptr - other.ptr) / mp3dec_frame_info_t.SIZE_BYTES
    @get:kotlin.jvm.JvmName("getmp3dec_frame_info_t") var CPointer<mp3dec_frame_info_t>.value: mp3dec_frame_info_t get() = this[0]; set(value) { this[0] = value }
    var mp3dec_frame_info_t.frame_bytes: Int get() = lw(ptr + mp3dec_frame_info_t.OFFSET_frame_bytes); set(value) = sw(ptr + mp3dec_frame_info_t.OFFSET_frame_bytes, value)
    var mp3dec_frame_info_t.channels: Int get() = lw(ptr + mp3dec_frame_info_t.OFFSET_channels); set(value) = sw(ptr + mp3dec_frame_info_t.OFFSET_channels, value)
    var mp3dec_frame_info_t.hz: Int get() = lw(ptr + mp3dec_frame_info_t.OFFSET_hz); set(value) = sw(ptr + mp3dec_frame_info_t.OFFSET_hz, value)
    var mp3dec_frame_info_t.layer: Int get() = lw(ptr + mp3dec_frame_info_t.OFFSET_layer); set(value) = sw(ptr + mp3dec_frame_info_t.OFFSET_layer, value)
    var mp3dec_frame_info_t.bitrate_kbps: Int get() = lw(ptr + mp3dec_frame_info_t.OFFSET_bitrate_kbps); set(value) = sw(ptr + mp3dec_frame_info_t.OFFSET_bitrate_kbps, value)
    fun mp3dec_tAlloc(): mp3dec_t = mp3dec_t(alloca(mp3dec_t.SIZE_BYTES).ptr)
    fun mp3dec_tAlloc(mdct_overlap: Array2Array288Float, qmf_state: Array960Float, reserv: Int, free_format_bytes: Int, header: Array4UByte, reserv_buf: Array511UByte): mp3dec_t = mp3dec_tAlloc().apply { this.mdct_overlap = mdct_overlap; this.qmf_state = qmf_state; this.reserv = reserv; this.free_format_bytes = free_format_bytes; this.header = header; this.reserv_buf = reserv_buf }
    fun mp3dec_t.copyFrom(src: mp3dec_t): mp3dec_t = this.apply { memcpy(CPointer<Unit>(this.ptr), CPointer<Unit>(src.ptr), mp3dec_t.SIZE_BYTES) }
    inline fun fixedArrayOfmp3dec_t(size: Int, setItems: CPointer<mp3dec_t>.() -> Unit): CPointer<mp3dec_t> = alloca_zero(size * mp3dec_t.SIZE_BYTES).toCPointer<mp3dec_t>().apply(setItems)
    @kotlin.jvm.JvmName("getmp3dec_t") operator fun CPointer<mp3dec_t>.get(index: Int): mp3dec_t = mp3dec_t(this.ptr + index * mp3dec_t.SIZE_BYTES)
    operator fun CPointer<mp3dec_t>.set(index: Int, value: mp3dec_t) = mp3dec_t(this.ptr + index * mp3dec_t.SIZE_BYTES).copyFrom(value)
    @kotlin.jvm.JvmName("plusmp3dec_t") operator fun CPointer<mp3dec_t>.plus(offset: Int): CPointer<mp3dec_t> = CPointer(this.ptr + offset * mp3dec_t.SIZE_BYTES)
    @kotlin.jvm.JvmName("minusmp3dec_t") operator fun CPointer<mp3dec_t>.minus(offset: Int): CPointer<mp3dec_t> = CPointer(this.ptr - offset * mp3dec_t.SIZE_BYTES)
    fun CPointer<mp3dec_t>.minusPtrmp3dec_t(other: CPointer<mp3dec_t>) = (this.ptr - other.ptr) / mp3dec_t.SIZE_BYTES
    @get:kotlin.jvm.JvmName("getmp3dec_t") var CPointer<mp3dec_t>.value: mp3dec_t get() = this[0]; set(value) { this[0] = value }
    var mp3dec_t.mdct_overlap: Array2Array288Float get() = Array2Array288Float(ptr + mp3dec_t.OFFSET_mdct_overlap); set(value) { TODO("Unsupported setting ftype=Float[288][2]") }
    var mp3dec_t.qmf_state: Array960Float get() = Array960Float(ptr + mp3dec_t.OFFSET_qmf_state); set(value) { TODO("Unsupported setting ftype=Float[960]") }
    var mp3dec_t.reserv: Int get() = lw(ptr + mp3dec_t.OFFSET_reserv); set(value) = sw(ptr + mp3dec_t.OFFSET_reserv, value)
    var mp3dec_t.free_format_bytes: Int get() = lw(ptr + mp3dec_t.OFFSET_free_format_bytes); set(value) = sw(ptr + mp3dec_t.OFFSET_free_format_bytes, value)
    var mp3dec_t.header: Array4UByte get() = Array4UByte(ptr + mp3dec_t.OFFSET_header); set(value) { TODO("Unsupported setting ftype=UByte[4]") }
    var mp3dec_t.reserv_buf: Array511UByte get() = Array511UByte(ptr + mp3dec_t.OFFSET_reserv_buf); set(value) { TODO("Unsupported setting ftype=UByte[511]") }
    fun bs_tAlloc(): bs_t = bs_t(alloca(bs_t.SIZE_BYTES).ptr)
    fun bs_tAlloc(buf: CPointer<UByte>, pos: Int, limit: Int): bs_t = bs_tAlloc().apply { this.buf = buf; this.pos = pos; this.limit = limit }
    fun bs_t.copyFrom(src: bs_t): bs_t = this.apply { memcpy(CPointer<Unit>(this.ptr), CPointer<Unit>(src.ptr), bs_t.SIZE_BYTES) }
    inline fun fixedArrayOfbs_t(size: Int, setItems: CPointer<bs_t>.() -> Unit): CPointer<bs_t> = alloca_zero(size * bs_t.SIZE_BYTES).toCPointer<bs_t>().apply(setItems)
    @kotlin.jvm.JvmName("getbs_t") operator fun CPointer<bs_t>.get(index: Int): bs_t = bs_t(this.ptr + index * bs_t.SIZE_BYTES)
    operator fun CPointer<bs_t>.set(index: Int, value: bs_t) = bs_t(this.ptr + index * bs_t.SIZE_BYTES).copyFrom(value)
    @kotlin.jvm.JvmName("plusbs_t") operator fun CPointer<bs_t>.plus(offset: Int): CPointer<bs_t> = CPointer(this.ptr + offset * bs_t.SIZE_BYTES)
    @kotlin.jvm.JvmName("minusbs_t") operator fun CPointer<bs_t>.minus(offset: Int): CPointer<bs_t> = CPointer(this.ptr - offset * bs_t.SIZE_BYTES)
    fun CPointer<bs_t>.minusPtrbs_t(other: CPointer<bs_t>) = (this.ptr - other.ptr) / bs_t.SIZE_BYTES
    @get:kotlin.jvm.JvmName("getbs_t") var CPointer<bs_t>.value: bs_t get() = this[0]; set(value) { this[0] = value }
    var bs_t.buf: CPointer<UByte> get() = CPointer(lw(ptr + bs_t.OFFSET_buf)); set(value) { sw(ptr + bs_t.OFFSET_buf, value.ptr) }
    var bs_t.pos: Int get() = lw(ptr + bs_t.OFFSET_pos); set(value) = sw(ptr + bs_t.OFFSET_pos, value)
    var bs_t.limit: Int get() = lw(ptr + bs_t.OFFSET_limit); set(value) = sw(ptr + bs_t.OFFSET_limit, value)
    fun L12_scale_infoAlloc(): L12_scale_info = L12_scale_info(alloca(L12_scale_info.SIZE_BYTES).ptr)
    fun L12_scale_infoAlloc(scf: Array192Float, total_bands: UByte, stereo_bands: UByte, bitalloc: Array64UByte, scfcod: Array64UByte): L12_scale_info = L12_scale_infoAlloc().apply { this.scf = scf; this.total_bands = total_bands; this.stereo_bands = stereo_bands; this.bitalloc = bitalloc; this.scfcod = scfcod }
    fun L12_scale_info.copyFrom(src: L12_scale_info): L12_scale_info = this.apply { memcpy(CPointer<Unit>(this.ptr), CPointer<Unit>(src.ptr), L12_scale_info.SIZE_BYTES) }
    inline fun fixedArrayOfL12_scale_info(size: Int, setItems: CPointer<L12_scale_info>.() -> Unit): CPointer<L12_scale_info> = alloca_zero(size * L12_scale_info.SIZE_BYTES).toCPointer<L12_scale_info>().apply(setItems)
    @kotlin.jvm.JvmName("getL12_scale_info") operator fun CPointer<L12_scale_info>.get(index: Int): L12_scale_info = L12_scale_info(this.ptr + index * L12_scale_info.SIZE_BYTES)
    operator fun CPointer<L12_scale_info>.set(index: Int, value: L12_scale_info) = L12_scale_info(this.ptr + index * L12_scale_info.SIZE_BYTES).copyFrom(value)
    @kotlin.jvm.JvmName("plusL12_scale_info") operator fun CPointer<L12_scale_info>.plus(offset: Int): CPointer<L12_scale_info> = CPointer(this.ptr + offset * L12_scale_info.SIZE_BYTES)
    @kotlin.jvm.JvmName("minusL12_scale_info") operator fun CPointer<L12_scale_info>.minus(offset: Int): CPointer<L12_scale_info> = CPointer(this.ptr - offset * L12_scale_info.SIZE_BYTES)
    fun CPointer<L12_scale_info>.minusPtrL12_scale_info(other: CPointer<L12_scale_info>) = (this.ptr - other.ptr) / L12_scale_info.SIZE_BYTES
    @get:kotlin.jvm.JvmName("getL12_scale_info") var CPointer<L12_scale_info>.value: L12_scale_info get() = this[0]; set(value) { this[0] = value }
    var L12_scale_info.scf: Array192Float get() = Array192Float(ptr + L12_scale_info.OFFSET_scf); set(value) { TODO("Unsupported setting ftype=Float[192]") }
    var L12_scale_info.total_bands: UByte get() = lb(ptr + L12_scale_info.OFFSET_total_bands).toUByte(); set(value) = sb(ptr + L12_scale_info.OFFSET_total_bands, (value).toByte())
    var L12_scale_info.stereo_bands: UByte get() = lb(ptr + L12_scale_info.OFFSET_stereo_bands).toUByte(); set(value) = sb(ptr + L12_scale_info.OFFSET_stereo_bands, (value).toByte())
    var L12_scale_info.bitalloc: Array64UByte get() = Array64UByte(ptr + L12_scale_info.OFFSET_bitalloc); set(value) { TODO("Unsupported setting ftype=UByte[64]") }
    var L12_scale_info.scfcod: Array64UByte get() = Array64UByte(ptr + L12_scale_info.OFFSET_scfcod); set(value) { TODO("Unsupported setting ftype=UByte[64]") }
    fun L12_subband_alloc_tAlloc(): L12_subband_alloc_t = L12_subband_alloc_t(alloca(L12_subband_alloc_t.SIZE_BYTES).ptr)
    fun L12_subband_alloc_tAlloc(tab_offset: UByte, code_tab_width: UByte, band_count: UByte): L12_subband_alloc_t = L12_subband_alloc_tAlloc().apply { this.tab_offset = tab_offset; this.code_tab_width = code_tab_width; this.band_count = band_count }
    fun L12_subband_alloc_t.copyFrom(src: L12_subband_alloc_t): L12_subband_alloc_t = this.apply { memcpy(CPointer<Unit>(this.ptr), CPointer<Unit>(src.ptr), L12_subband_alloc_t.SIZE_BYTES) }
    inline fun fixedArrayOfL12_subband_alloc_t(size: Int, setItems: CPointer<L12_subband_alloc_t>.() -> Unit): CPointer<L12_subband_alloc_t> = alloca_zero(size * L12_subband_alloc_t.SIZE_BYTES).toCPointer<L12_subband_alloc_t>().apply(setItems)
    @kotlin.jvm.JvmName("getL12_subband_alloc_t") operator fun CPointer<L12_subband_alloc_t>.get(index: Int): L12_subband_alloc_t = L12_subband_alloc_t(this.ptr + index * L12_subband_alloc_t.SIZE_BYTES)
    operator fun CPointer<L12_subband_alloc_t>.set(index: Int, value: L12_subband_alloc_t) = L12_subband_alloc_t(this.ptr + index * L12_subband_alloc_t.SIZE_BYTES).copyFrom(value)
    @kotlin.jvm.JvmName("plusL12_subband_alloc_t") operator fun CPointer<L12_subband_alloc_t>.plus(offset: Int): CPointer<L12_subband_alloc_t> = CPointer(this.ptr + offset * L12_subband_alloc_t.SIZE_BYTES)
    @kotlin.jvm.JvmName("minusL12_subband_alloc_t") operator fun CPointer<L12_subband_alloc_t>.minus(offset: Int): CPointer<L12_subband_alloc_t> = CPointer(this.ptr - offset * L12_subband_alloc_t.SIZE_BYTES)
    fun CPointer<L12_subband_alloc_t>.minusPtrL12_subband_alloc_t(other: CPointer<L12_subband_alloc_t>) = (this.ptr - other.ptr) / L12_subband_alloc_t.SIZE_BYTES
    @get:kotlin.jvm.JvmName("getL12_subband_alloc_t") var CPointer<L12_subband_alloc_t>.value: L12_subband_alloc_t get() = this[0]; set(value) { this[0] = value }
    var L12_subband_alloc_t.tab_offset: UByte get() = lb(ptr + L12_subband_alloc_t.OFFSET_tab_offset).toUByte(); set(value) = sb(ptr + L12_subband_alloc_t.OFFSET_tab_offset, (value).toByte())
    var L12_subband_alloc_t.code_tab_width: UByte get() = lb(ptr + L12_subband_alloc_t.OFFSET_code_tab_width).toUByte(); set(value) = sb(ptr + L12_subband_alloc_t.OFFSET_code_tab_width, (value).toByte())
    var L12_subband_alloc_t.band_count: UByte get() = lb(ptr + L12_subband_alloc_t.OFFSET_band_count).toUByte(); set(value) = sb(ptr + L12_subband_alloc_t.OFFSET_band_count, (value).toByte())
    fun L3_gr_info_tAlloc(): L3_gr_info_t = L3_gr_info_t(alloca(L3_gr_info_t.SIZE_BYTES).ptr)
    fun L3_gr_info_tAlloc(sfbtab: CPointer<UByte>, part_23_length: UShort, big_values: UShort, scalefac_compress: UShort, global_gain: UByte, block_type: UByte, mixed_block_flag: UByte, n_long_sfb: UByte, n_short_sfb: UByte, table_select: Array3UByte, region_count: Array3UByte, subblock_gain: Array3UByte, preflag: UByte, scalefac_scale: UByte, count1_table: UByte, scfsi: UByte): L3_gr_info_t = L3_gr_info_tAlloc().apply { this.sfbtab = sfbtab; this.part_23_length = part_23_length; this.big_values = big_values; this.scalefac_compress = scalefac_compress; this.global_gain = global_gain; this.block_type = block_type; this.mixed_block_flag = mixed_block_flag; this.n_long_sfb = n_long_sfb; this.n_short_sfb = n_short_sfb; this.table_select = table_select; this.region_count = region_count; this.subblock_gain = subblock_gain; this.preflag = preflag; this.scalefac_scale = scalefac_scale; this.count1_table = count1_table; this.scfsi = scfsi }
    fun L3_gr_info_t.copyFrom(src: L3_gr_info_t): L3_gr_info_t = this.apply { memcpy(CPointer<Unit>(this.ptr), CPointer<Unit>(src.ptr), L3_gr_info_t.SIZE_BYTES) }
    inline fun fixedArrayOfL3_gr_info_t(size: Int, setItems: CPointer<L3_gr_info_t>.() -> Unit): CPointer<L3_gr_info_t> = alloca_zero(size * L3_gr_info_t.SIZE_BYTES).toCPointer<L3_gr_info_t>().apply(setItems)
    @kotlin.jvm.JvmName("getL3_gr_info_t") operator fun CPointer<L3_gr_info_t>.get(index: Int): L3_gr_info_t = L3_gr_info_t(this.ptr + index * L3_gr_info_t.SIZE_BYTES)
    operator fun CPointer<L3_gr_info_t>.set(index: Int, value: L3_gr_info_t) = L3_gr_info_t(this.ptr + index * L3_gr_info_t.SIZE_BYTES).copyFrom(value)
    @kotlin.jvm.JvmName("plusL3_gr_info_t") operator fun CPointer<L3_gr_info_t>.plus(offset: Int): CPointer<L3_gr_info_t> = CPointer(this.ptr + offset * L3_gr_info_t.SIZE_BYTES)
    @kotlin.jvm.JvmName("minusL3_gr_info_t") operator fun CPointer<L3_gr_info_t>.minus(offset: Int): CPointer<L3_gr_info_t> = CPointer(this.ptr - offset * L3_gr_info_t.SIZE_BYTES)
    fun CPointer<L3_gr_info_t>.minusPtrL3_gr_info_t(other: CPointer<L3_gr_info_t>) = (this.ptr - other.ptr) / L3_gr_info_t.SIZE_BYTES
    @get:kotlin.jvm.JvmName("getL3_gr_info_t") var CPointer<L3_gr_info_t>.value: L3_gr_info_t get() = this[0]; set(value) { this[0] = value }
    var L3_gr_info_t.sfbtab: CPointer<UByte> get() = CPointer(lw(ptr + L3_gr_info_t.OFFSET_sfbtab)); set(value) { sw(ptr + L3_gr_info_t.OFFSET_sfbtab, value.ptr) }
    var L3_gr_info_t.part_23_length: UShort get() = lh(ptr + L3_gr_info_t.OFFSET_part_23_length).toUShort(); set(value) = sh(ptr + L3_gr_info_t.OFFSET_part_23_length, (value).toShort())
    var L3_gr_info_t.big_values: UShort get() = lh(ptr + L3_gr_info_t.OFFSET_big_values).toUShort(); set(value) = sh(ptr + L3_gr_info_t.OFFSET_big_values, (value).toShort())
    var L3_gr_info_t.scalefac_compress: UShort get() = lh(ptr + L3_gr_info_t.OFFSET_scalefac_compress).toUShort(); set(value) = sh(ptr + L3_gr_info_t.OFFSET_scalefac_compress, (value).toShort())
    var L3_gr_info_t.global_gain: UByte get() = lb(ptr + L3_gr_info_t.OFFSET_global_gain).toUByte(); set(value) = sb(ptr + L3_gr_info_t.OFFSET_global_gain, (value).toByte())
    var L3_gr_info_t.block_type: UByte get() = lb(ptr + L3_gr_info_t.OFFSET_block_type).toUByte(); set(value) = sb(ptr + L3_gr_info_t.OFFSET_block_type, (value).toByte())
    var L3_gr_info_t.mixed_block_flag: UByte get() = lb(ptr + L3_gr_info_t.OFFSET_mixed_block_flag).toUByte(); set(value) = sb(ptr + L3_gr_info_t.OFFSET_mixed_block_flag, (value).toByte())
    var L3_gr_info_t.n_long_sfb: UByte get() = lb(ptr + L3_gr_info_t.OFFSET_n_long_sfb).toUByte(); set(value) = sb(ptr + L3_gr_info_t.OFFSET_n_long_sfb, (value).toByte())
    var L3_gr_info_t.n_short_sfb: UByte get() = lb(ptr + L3_gr_info_t.OFFSET_n_short_sfb).toUByte(); set(value) = sb(ptr + L3_gr_info_t.OFFSET_n_short_sfb, (value).toByte())
    var L3_gr_info_t.table_select: Array3UByte get() = Array3UByte(ptr + L3_gr_info_t.OFFSET_table_select); set(value) { TODO("Unsupported setting ftype=UByte[3]") }
    var L3_gr_info_t.region_count: Array3UByte get() = Array3UByte(ptr + L3_gr_info_t.OFFSET_region_count); set(value) { TODO("Unsupported setting ftype=UByte[3]") }
    var L3_gr_info_t.subblock_gain: Array3UByte get() = Array3UByte(ptr + L3_gr_info_t.OFFSET_subblock_gain); set(value) { TODO("Unsupported setting ftype=UByte[3]") }
    var L3_gr_info_t.preflag: UByte get() = lb(ptr + L3_gr_info_t.OFFSET_preflag).toUByte(); set(value) = sb(ptr + L3_gr_info_t.OFFSET_preflag, (value).toByte())
    var L3_gr_info_t.scalefac_scale: UByte get() = lb(ptr + L3_gr_info_t.OFFSET_scalefac_scale).toUByte(); set(value) = sb(ptr + L3_gr_info_t.OFFSET_scalefac_scale, (value).toByte())
    var L3_gr_info_t.count1_table: UByte get() = lb(ptr + L3_gr_info_t.OFFSET_count1_table).toUByte(); set(value) = sb(ptr + L3_gr_info_t.OFFSET_count1_table, (value).toByte())
    var L3_gr_info_t.scfsi: UByte get() = lb(ptr + L3_gr_info_t.OFFSET_scfsi).toUByte(); set(value) = sb(ptr + L3_gr_info_t.OFFSET_scfsi, (value).toByte())
    fun mp3dec_scratch_tAlloc(): mp3dec_scratch_t = mp3dec_scratch_t(alloca(mp3dec_scratch_t.SIZE_BYTES).ptr)
    fun mp3dec_scratch_tAlloc(bs: bs_t, maindata: Array2815UByte, gr_info: Array4L3_gr_info_t, grbuf: Array2Array576Float, scf: Array40Float, syn: Array33Array64Float, ist_pos: Array2Array39UByte): mp3dec_scratch_t = mp3dec_scratch_tAlloc().apply { this.bs = bs; this.maindata = maindata; this.gr_info = gr_info; this.grbuf = grbuf; this.scf = scf; this.syn = syn; this.ist_pos = ist_pos }
    fun mp3dec_scratch_t.copyFrom(src: mp3dec_scratch_t): mp3dec_scratch_t = this.apply { memcpy(CPointer<Unit>(this.ptr), CPointer<Unit>(src.ptr), mp3dec_scratch_t.SIZE_BYTES) }
    inline fun fixedArrayOfmp3dec_scratch_t(size: Int, setItems: CPointer<mp3dec_scratch_t>.() -> Unit): CPointer<mp3dec_scratch_t> = alloca_zero(size * mp3dec_scratch_t.SIZE_BYTES).toCPointer<mp3dec_scratch_t>().apply(setItems)
    @kotlin.jvm.JvmName("getmp3dec_scratch_t") operator fun CPointer<mp3dec_scratch_t>.get(index: Int): mp3dec_scratch_t = mp3dec_scratch_t(this.ptr + index * mp3dec_scratch_t.SIZE_BYTES)
    operator fun CPointer<mp3dec_scratch_t>.set(index: Int, value: mp3dec_scratch_t) = mp3dec_scratch_t(this.ptr + index * mp3dec_scratch_t.SIZE_BYTES).copyFrom(value)
    @kotlin.jvm.JvmName("plusmp3dec_scratch_t") operator fun CPointer<mp3dec_scratch_t>.plus(offset: Int): CPointer<mp3dec_scratch_t> = CPointer(this.ptr + offset * mp3dec_scratch_t.SIZE_BYTES)
    @kotlin.jvm.JvmName("minusmp3dec_scratch_t") operator fun CPointer<mp3dec_scratch_t>.minus(offset: Int): CPointer<mp3dec_scratch_t> = CPointer(this.ptr - offset * mp3dec_scratch_t.SIZE_BYTES)
    fun CPointer<mp3dec_scratch_t>.minusPtrmp3dec_scratch_t(other: CPointer<mp3dec_scratch_t>) = (this.ptr - other.ptr) / mp3dec_scratch_t.SIZE_BYTES
    @get:kotlin.jvm.JvmName("getmp3dec_scratch_t") var CPointer<mp3dec_scratch_t>.value: mp3dec_scratch_t get() = this[0]; set(value) { this[0] = value }
    var mp3dec_scratch_t.bs: bs_t get() = bs_t(ptr + mp3dec_scratch_t.OFFSET_bs); set(value) { bs_t(ptr + mp3dec_scratch_t.OFFSET_bs).copyFrom(value) }
    var mp3dec_scratch_t.maindata: Array2815UByte get() = Array2815UByte(ptr + mp3dec_scratch_t.OFFSET_maindata); set(value) { TODO("Unsupported setting ftype=UByte[2815]") }
    var mp3dec_scratch_t.gr_info: Array4L3_gr_info_t get() = Array4L3_gr_info_t(ptr + mp3dec_scratch_t.OFFSET_gr_info); set(value) { TODO("Unsupported setting ftype=struct null[4]") }
    var mp3dec_scratch_t.grbuf: Array2Array576Float get() = Array2Array576Float(ptr + mp3dec_scratch_t.OFFSET_grbuf); set(value) { TODO("Unsupported setting ftype=Float[576][2]") }
    var mp3dec_scratch_t.scf: Array40Float get() = Array40Float(ptr + mp3dec_scratch_t.OFFSET_scf); set(value) { TODO("Unsupported setting ftype=Float[40]") }
    var mp3dec_scratch_t.syn: Array33Array64Float get() = Array33Array64Float(ptr + mp3dec_scratch_t.OFFSET_syn); set(value) { TODO("Unsupported setting ftype=Float[64][33]") }
    var mp3dec_scratch_t.ist_pos: Array2Array39UByte get() = Array2Array39UByte(ptr + mp3dec_scratch_t.OFFSET_ist_pos); set(value) { TODO("Unsupported setting ftype=UByte[39][2]") }
    operator fun Array2Array288Float.get(index: Int): Array288Float = Array288Float(addr(index))
    operator fun Array2Array288Float.set(index: Int, value: Array288Float): Unit { memcpy(CPointer(addr(index)), CPointer(value.ptr), Array2Array288Float.ELEMENT_SIZE_BYTES) }
    var Array2Array288Float.value get() = this[0]; set(value) { this[0] = value }
    inline fun Array2Array288FloatAlloc(setItems: Array2Array288Float.() -> Unit): Array2Array288Float = Array2Array288Float(alloca_zero(Array2Array288Float.TOTAL_SIZE_BYTES).ptr).apply(setItems)
    operator fun Array2Array288Float.plus(offset: Int): CPointer<Array288Float> = CPointer<Array288Float>(addr(offset))
    operator fun Array2Array288Float.minus(offset: Int): CPointer<Array288Float> = CPointer<Array288Float>(addr(-offset))
    operator fun Array288Float.get(index: Int): Float = lwf(addr(index))
    operator fun Array288Float.set(index: Int, value: Float): Unit { swf(addr(index), (value)) }
    var Array288Float.value get() = this[0]; set(value) { this[0] = value }
    inline fun Array288FloatAlloc(setItems: Array288Float.() -> Unit): Array288Float = Array288Float(alloca_zero(Array288Float.TOTAL_SIZE_BYTES).ptr).apply(setItems)
    operator fun Array288Float.plus(offset: Int): CPointer<Float> = CPointer<Float>(addr(offset))
    operator fun Array288Float.minus(offset: Int): CPointer<Float> = CPointer<Float>(addr(-offset))
    operator fun Array960Float.get(index: Int): Float = lwf(addr(index))
    operator fun Array960Float.set(index: Int, value: Float): Unit { swf(addr(index), (value)) }
    var Array960Float.value get() = this[0]; set(value) { this[0] = value }
    inline fun Array960FloatAlloc(setItems: Array960Float.() -> Unit): Array960Float = Array960Float(alloca_zero(Array960Float.TOTAL_SIZE_BYTES).ptr).apply(setItems)
    operator fun Array960Float.plus(offset: Int): CPointer<Float> = CPointer<Float>(addr(offset))
    operator fun Array960Float.minus(offset: Int): CPointer<Float> = CPointer<Float>(addr(-offset))
    operator fun Array4UByte.get(index: Int): UByte = lb(addr(index)).toUByte()
    operator fun Array4UByte.set(index: Int, value: UByte): Unit { sb(addr(index), (value).toByte()) }
    var Array4UByte.value get() = this[0]; set(value) { this[0] = value }
    inline fun Array4UByteAlloc(setItems: Array4UByte.() -> Unit): Array4UByte = Array4UByte(alloca_zero(Array4UByte.TOTAL_SIZE_BYTES).ptr).apply(setItems)
    operator fun Array4UByte.plus(offset: Int): CPointer<UByte> = CPointer<UByte>(addr(offset))
    operator fun Array4UByte.minus(offset: Int): CPointer<UByte> = CPointer<UByte>(addr(-offset))
    operator fun Array511UByte.get(index: Int): UByte = lb(addr(index)).toUByte()
    operator fun Array511UByte.set(index: Int, value: UByte): Unit { sb(addr(index), (value).toByte()) }
    var Array511UByte.value get() = this[0]; set(value) { this[0] = value }
    inline fun Array511UByteAlloc(setItems: Array511UByte.() -> Unit): Array511UByte = Array511UByte(alloca_zero(Array511UByte.TOTAL_SIZE_BYTES).ptr).apply(setItems)
    operator fun Array511UByte.plus(offset: Int): CPointer<UByte> = CPointer<UByte>(addr(offset))
    operator fun Array511UByte.minus(offset: Int): CPointer<UByte> = CPointer<UByte>(addr(-offset))
    operator fun Array192Float.get(index: Int): Float = lwf(addr(index))
    operator fun Array192Float.set(index: Int, value: Float): Unit { swf(addr(index), (value)) }
    var Array192Float.value get() = this[0]; set(value) { this[0] = value }
    inline fun Array192FloatAlloc(setItems: Array192Float.() -> Unit): Array192Float = Array192Float(alloca_zero(Array192Float.TOTAL_SIZE_BYTES).ptr).apply(setItems)
    operator fun Array192Float.plus(offset: Int): CPointer<Float> = CPointer<Float>(addr(offset))
    operator fun Array192Float.minus(offset: Int): CPointer<Float> = CPointer<Float>(addr(-offset))
    operator fun Array64UByte.get(index: Int): UByte = lb(addr(index)).toUByte()
    operator fun Array64UByte.set(index: Int, value: UByte): Unit { sb(addr(index), (value).toByte()) }
    var Array64UByte.value get() = this[0]; set(value) { this[0] = value }
    inline fun Array64UByteAlloc(setItems: Array64UByte.() -> Unit): Array64UByte = Array64UByte(alloca_zero(Array64UByte.TOTAL_SIZE_BYTES).ptr).apply(setItems)
    operator fun Array64UByte.plus(offset: Int): CPointer<UByte> = CPointer<UByte>(addr(offset))
    operator fun Array64UByte.minus(offset: Int): CPointer<UByte> = CPointer<UByte>(addr(-offset))
    operator fun Array3UByte.get(index: Int): UByte = lb(addr(index)).toUByte()
    operator fun Array3UByte.set(index: Int, value: UByte): Unit { sb(addr(index), (value).toByte()) }
    var Array3UByte.value get() = this[0]; set(value) { this[0] = value }
    inline fun Array3UByteAlloc(setItems: Array3UByte.() -> Unit): Array3UByte = Array3UByte(alloca_zero(Array3UByte.TOTAL_SIZE_BYTES).ptr).apply(setItems)
    operator fun Array3UByte.plus(offset: Int): CPointer<UByte> = CPointer<UByte>(addr(offset))
    operator fun Array3UByte.minus(offset: Int): CPointer<UByte> = CPointer<UByte>(addr(-offset))
    operator fun Array2815UByte.get(index: Int): UByte = lb(addr(index)).toUByte()
    operator fun Array2815UByte.set(index: Int, value: UByte): Unit { sb(addr(index), (value).toByte()) }
    var Array2815UByte.value get() = this[0]; set(value) { this[0] = value }
    inline fun Array2815UByteAlloc(setItems: Array2815UByte.() -> Unit): Array2815UByte = Array2815UByte(alloca_zero(Array2815UByte.TOTAL_SIZE_BYTES).ptr).apply(setItems)
    operator fun Array2815UByte.plus(offset: Int): CPointer<UByte> = CPointer<UByte>(addr(offset))
    operator fun Array2815UByte.minus(offset: Int): CPointer<UByte> = CPointer<UByte>(addr(-offset))
    operator fun Array4L3_gr_info_t.get(index: Int): L3_gr_info_t = L3_gr_info_t(addr(index))
    operator fun Array4L3_gr_info_t.set(index: Int, value: L3_gr_info_t): Unit { L3_gr_info_t(addr(index)).copyFrom(value) }
    var Array4L3_gr_info_t.value get() = this[0]; set(value) { this[0] = value }
    inline fun Array4L3_gr_info_tAlloc(setItems: Array4L3_gr_info_t.() -> Unit): Array4L3_gr_info_t = Array4L3_gr_info_t(alloca_zero(Array4L3_gr_info_t.TOTAL_SIZE_BYTES).ptr).apply(setItems)
    operator fun Array4L3_gr_info_t.plus(offset: Int): CPointer<L3_gr_info_t> = CPointer<L3_gr_info_t>(addr(offset))
    operator fun Array4L3_gr_info_t.minus(offset: Int): CPointer<L3_gr_info_t> = CPointer<L3_gr_info_t>(addr(-offset))
    operator fun Array2Array576Float.get(index: Int): Array576Float = Array576Float(addr(index))
    operator fun Array2Array576Float.set(index: Int, value: Array576Float): Unit { memcpy(CPointer(addr(index)), CPointer(value.ptr), Array2Array576Float.ELEMENT_SIZE_BYTES) }
    var Array2Array576Float.value get() = this[0]; set(value) { this[0] = value }
    inline fun Array2Array576FloatAlloc(setItems: Array2Array576Float.() -> Unit): Array2Array576Float = Array2Array576Float(alloca_zero(Array2Array576Float.TOTAL_SIZE_BYTES).ptr).apply(setItems)
    operator fun Array2Array576Float.plus(offset: Int): CPointer<Array576Float> = CPointer<Array576Float>(addr(offset))
    operator fun Array2Array576Float.minus(offset: Int): CPointer<Array576Float> = CPointer<Array576Float>(addr(-offset))
    operator fun Array576Float.get(index: Int): Float = lwf(addr(index))
    operator fun Array576Float.set(index: Int, value: Float): Unit { swf(addr(index), (value)) }
    var Array576Float.value get() = this[0]; set(value) { this[0] = value }
    inline fun Array576FloatAlloc(setItems: Array576Float.() -> Unit): Array576Float = Array576Float(alloca_zero(Array576Float.TOTAL_SIZE_BYTES).ptr).apply(setItems)
    operator fun Array576Float.plus(offset: Int): CPointer<Float> = CPointer<Float>(addr(offset))
    operator fun Array576Float.minus(offset: Int): CPointer<Float> = CPointer<Float>(addr(-offset))
    operator fun Array40Float.get(index: Int): Float = lwf(addr(index))
    operator fun Array40Float.set(index: Int, value: Float): Unit { swf(addr(index), (value)) }
    var Array40Float.value get() = this[0]; set(value) { this[0] = value }
    inline fun Array40FloatAlloc(setItems: Array40Float.() -> Unit): Array40Float = Array40Float(alloca_zero(Array40Float.TOTAL_SIZE_BYTES).ptr).apply(setItems)
    operator fun Array40Float.plus(offset: Int): CPointer<Float> = CPointer<Float>(addr(offset))
    operator fun Array40Float.minus(offset: Int): CPointer<Float> = CPointer<Float>(addr(-offset))
    operator fun Array33Array64Float.get(index: Int): Array64Float = Array64Float(addr(index))
    operator fun Array33Array64Float.set(index: Int, value: Array64Float): Unit { memcpy(CPointer(addr(index)), CPointer(value.ptr), Array33Array64Float.ELEMENT_SIZE_BYTES) }
    var Array33Array64Float.value get() = this[0]; set(value) { this[0] = value }
    inline fun Array33Array64FloatAlloc(setItems: Array33Array64Float.() -> Unit): Array33Array64Float = Array33Array64Float(alloca_zero(Array33Array64Float.TOTAL_SIZE_BYTES).ptr).apply(setItems)
    operator fun Array33Array64Float.plus(offset: Int): CPointer<Array64Float> = CPointer<Array64Float>(addr(offset))
    operator fun Array33Array64Float.minus(offset: Int): CPointer<Array64Float> = CPointer<Array64Float>(addr(-offset))
    operator fun Array64Float.get(index: Int): Float = lwf(addr(index))
    operator fun Array64Float.set(index: Int, value: Float): Unit { swf(addr(index), (value)) }
    var Array64Float.value get() = this[0]; set(value) { this[0] = value }
    inline fun Array64FloatAlloc(setItems: Array64Float.() -> Unit): Array64Float = Array64Float(alloca_zero(Array64Float.TOTAL_SIZE_BYTES).ptr).apply(setItems)
    operator fun Array64Float.plus(offset: Int): CPointer<Float> = CPointer<Float>(addr(offset))
    operator fun Array64Float.minus(offset: Int): CPointer<Float> = CPointer<Float>(addr(-offset))
    operator fun Array2Array39UByte.get(index: Int): Array39UByte = Array39UByte(addr(index))
    operator fun Array2Array39UByte.set(index: Int, value: Array39UByte): Unit { memcpy(CPointer(addr(index)), CPointer(value.ptr), Array2Array39UByte.ELEMENT_SIZE_BYTES) }
    var Array2Array39UByte.value get() = this[0]; set(value) { this[0] = value }
    inline fun Array2Array39UByteAlloc(setItems: Array2Array39UByte.() -> Unit): Array2Array39UByte = Array2Array39UByte(alloca_zero(Array2Array39UByte.TOTAL_SIZE_BYTES).ptr).apply(setItems)
    operator fun Array2Array39UByte.plus(offset: Int): CPointer<Array39UByte> = CPointer<Array39UByte>(addr(offset))
    operator fun Array2Array39UByte.minus(offset: Int): CPointer<Array39UByte> = CPointer<Array39UByte>(addr(-offset))
    operator fun Array39UByte.get(index: Int): UByte = lb(addr(index)).toUByte()
    operator fun Array39UByte.set(index: Int, value: UByte): Unit { sb(addr(index), (value).toByte()) }
    var Array39UByte.value get() = this[0]; set(value) { this[0] = value }
    inline fun Array39UByteAlloc(setItems: Array39UByte.() -> Unit): Array39UByte = Array39UByte(alloca_zero(Array39UByte.TOTAL_SIZE_BYTES).ptr).apply(setItems)
    operator fun Array39UByte.plus(offset: Int): CPointer<UByte> = CPointer<UByte>(addr(offset))
    operator fun Array39UByte.minus(offset: Int): CPointer<UByte> = CPointer<UByte>(addr(-offset))
    operator fun Array2Array3Array15UByte.get(index: Int): Array3Array15UByte = Array3Array15UByte(addr(index))
    operator fun Array2Array3Array15UByte.set(index: Int, value: Array3Array15UByte): Unit { memcpy(CPointer(addr(index)), CPointer(value.ptr), Array2Array3Array15UByte.ELEMENT_SIZE_BYTES) }
    var Array2Array3Array15UByte.value get() = this[0]; set(value) { this[0] = value }
    inline fun Array2Array3Array15UByteAlloc(setItems: Array2Array3Array15UByte.() -> Unit): Array2Array3Array15UByte = Array2Array3Array15UByte(alloca_zero(Array2Array3Array15UByte.TOTAL_SIZE_BYTES).ptr).apply(setItems)
    operator fun Array2Array3Array15UByte.plus(offset: Int): CPointer<Array3Array15UByte> = CPointer<Array3Array15UByte>(addr(offset))
    operator fun Array2Array3Array15UByte.minus(offset: Int): CPointer<Array3Array15UByte> = CPointer<Array3Array15UByte>(addr(-offset))
    operator fun Array3Array15UByte.get(index: Int): Array15UByte = Array15UByte(addr(index))
    operator fun Array3Array15UByte.set(index: Int, value: Array15UByte): Unit { memcpy(CPointer(addr(index)), CPointer(value.ptr), Array3Array15UByte.ELEMENT_SIZE_BYTES) }
    var Array3Array15UByte.value get() = this[0]; set(value) { this[0] = value }
    inline fun Array3Array15UByteAlloc(setItems: Array3Array15UByte.() -> Unit): Array3Array15UByte = Array3Array15UByte(alloca_zero(Array3Array15UByte.TOTAL_SIZE_BYTES).ptr).apply(setItems)
    operator fun Array3Array15UByte.plus(offset: Int): CPointer<Array15UByte> = CPointer<Array15UByte>(addr(offset))
    operator fun Array3Array15UByte.minus(offset: Int): CPointer<Array15UByte> = CPointer<Array15UByte>(addr(-offset))
    operator fun Array15UByte.get(index: Int): UByte = lb(addr(index)).toUByte()
    operator fun Array15UByte.set(index: Int, value: UByte): Unit { sb(addr(index), (value).toByte()) }
    var Array15UByte.value get() = this[0]; set(value) { this[0] = value }
    inline fun Array15UByteAlloc(setItems: Array15UByte.() -> Unit): Array15UByte = Array15UByte(alloca_zero(Array15UByte.TOTAL_SIZE_BYTES).ptr).apply(setItems)
    operator fun Array15UByte.plus(offset: Int): CPointer<UByte> = CPointer<UByte>(addr(offset))
    operator fun Array15UByte.minus(offset: Int): CPointer<UByte> = CPointer<UByte>(addr(-offset))
    operator fun Array3UInt.get(index: Int): UInt = lw(addr(index)).toUInt()
    operator fun Array3UInt.set(index: Int, value: UInt): Unit { sw(addr(index), (value).toInt()) }
    var Array3UInt.value get() = this[0]; set(value) { this[0] = value }
    inline fun Array3UIntAlloc(setItems: Array3UInt.() -> Unit): Array3UInt = Array3UInt(alloca_zero(Array3UInt.TOTAL_SIZE_BYTES).ptr).apply(setItems)
    operator fun Array3UInt.plus(offset: Int): CPointer<UInt> = CPointer<UInt>(addr(offset))
    operator fun Array3UInt.minus(offset: Int): CPointer<UInt> = CPointer<UInt>(addr(-offset))
    operator fun Array54Float.get(index: Int): Float = lwf(addr(index))
    operator fun Array54Float.set(index: Int, value: Float): Unit { swf(addr(index), (value)) }
    var Array54Float.value get() = this[0]; set(value) { this[0] = value }
    inline fun Array54FloatAlloc(setItems: Array54Float.() -> Unit): Array54Float = Array54Float(alloca_zero(Array54Float.TOTAL_SIZE_BYTES).ptr).apply(setItems)
    operator fun Array54Float.plus(offset: Int): CPointer<Float> = CPointer<Float>(addr(offset))
    operator fun Array54Float.minus(offset: Int): CPointer<Float> = CPointer<Float>(addr(-offset))
    operator fun Array8Array23UByte.get(index: Int): Array23UByte = Array23UByte(addr(index))
    operator fun Array8Array23UByte.set(index: Int, value: Array23UByte): Unit { memcpy(CPointer(addr(index)), CPointer(value.ptr), Array8Array23UByte.ELEMENT_SIZE_BYTES) }
    var Array8Array23UByte.value get() = this[0]; set(value) { this[0] = value }
    inline fun Array8Array23UByteAlloc(setItems: Array8Array23UByte.() -> Unit): Array8Array23UByte = Array8Array23UByte(alloca_zero(Array8Array23UByte.TOTAL_SIZE_BYTES).ptr).apply(setItems)
    operator fun Array8Array23UByte.plus(offset: Int): CPointer<Array23UByte> = CPointer<Array23UByte>(addr(offset))
    operator fun Array8Array23UByte.minus(offset: Int): CPointer<Array23UByte> = CPointer<Array23UByte>(addr(-offset))
    operator fun Array23UByte.get(index: Int): UByte = lb(addr(index)).toUByte()
    operator fun Array23UByte.set(index: Int, value: UByte): Unit { sb(addr(index), (value).toByte()) }
    var Array23UByte.value get() = this[0]; set(value) { this[0] = value }
    inline fun Array23UByteAlloc(setItems: Array23UByte.() -> Unit): Array23UByte = Array23UByte(alloca_zero(Array23UByte.TOTAL_SIZE_BYTES).ptr).apply(setItems)
    operator fun Array23UByte.plus(offset: Int): CPointer<UByte> = CPointer<UByte>(addr(offset))
    operator fun Array23UByte.minus(offset: Int): CPointer<UByte> = CPointer<UByte>(addr(-offset))
    operator fun Array8Array40UByte.get(index: Int): Array40UByte = Array40UByte(addr(index))
    operator fun Array8Array40UByte.set(index: Int, value: Array40UByte): Unit { memcpy(CPointer(addr(index)), CPointer(value.ptr), Array8Array40UByte.ELEMENT_SIZE_BYTES) }
    var Array8Array40UByte.value get() = this[0]; set(value) { this[0] = value }
    inline fun Array8Array40UByteAlloc(setItems: Array8Array40UByte.() -> Unit): Array8Array40UByte = Array8Array40UByte(alloca_zero(Array8Array40UByte.TOTAL_SIZE_BYTES).ptr).apply(setItems)
    operator fun Array8Array40UByte.plus(offset: Int): CPointer<Array40UByte> = CPointer<Array40UByte>(addr(offset))
    operator fun Array8Array40UByte.minus(offset: Int): CPointer<Array40UByte> = CPointer<Array40UByte>(addr(-offset))
    operator fun Array40UByte.get(index: Int): UByte = lb(addr(index)).toUByte()
    operator fun Array40UByte.set(index: Int, value: UByte): Unit { sb(addr(index), (value).toByte()) }
    var Array40UByte.value get() = this[0]; set(value) { this[0] = value }
    inline fun Array40UByteAlloc(setItems: Array40UByte.() -> Unit): Array40UByte = Array40UByte(alloca_zero(Array40UByte.TOTAL_SIZE_BYTES).ptr).apply(setItems)
    operator fun Array40UByte.plus(offset: Int): CPointer<UByte> = CPointer<UByte>(addr(offset))
    operator fun Array40UByte.minus(offset: Int): CPointer<UByte> = CPointer<UByte>(addr(-offset))
    operator fun Array4Float.get(index: Int): Float = lwf(addr(index))
    operator fun Array4Float.set(index: Int, value: Float): Unit { swf(addr(index), (value)) }
    var Array4Float.value get() = this[0]; set(value) { this[0] = value }
    inline fun Array4FloatAlloc(setItems: Array4Float.() -> Unit): Array4Float = Array4Float(alloca_zero(Array4Float.TOTAL_SIZE_BYTES).ptr).apply(setItems)
    operator fun Array4Float.plus(offset: Int): CPointer<Float> = CPointer<Float>(addr(offset))
    operator fun Array4Float.minus(offset: Int): CPointer<Float> = CPointer<Float>(addr(-offset))
    operator fun Array3Array28UByte.get(index: Int): Array28UByte = Array28UByte(addr(index))
    operator fun Array3Array28UByte.set(index: Int, value: Array28UByte): Unit { memcpy(CPointer(addr(index)), CPointer(value.ptr), Array3Array28UByte.ELEMENT_SIZE_BYTES) }
    var Array3Array28UByte.value get() = this[0]; set(value) { this[0] = value }
    inline fun Array3Array28UByteAlloc(setItems: Array3Array28UByte.() -> Unit): Array3Array28UByte = Array3Array28UByte(alloca_zero(Array3Array28UByte.TOTAL_SIZE_BYTES).ptr).apply(setItems)
    operator fun Array3Array28UByte.plus(offset: Int): CPointer<Array28UByte> = CPointer<Array28UByte>(addr(offset))
    operator fun Array3Array28UByte.minus(offset: Int): CPointer<Array28UByte> = CPointer<Array28UByte>(addr(-offset))
    operator fun Array28UByte.get(index: Int): UByte = lb(addr(index)).toUByte()
    operator fun Array28UByte.set(index: Int, value: UByte): Unit { sb(addr(index), (value).toByte()) }
    var Array28UByte.value get() = this[0]; set(value) { this[0] = value }
    inline fun Array28UByteAlloc(setItems: Array28UByte.() -> Unit): Array28UByte = Array28UByte(alloca_zero(Array28UByte.TOTAL_SIZE_BYTES).ptr).apply(setItems)
    operator fun Array28UByte.plus(offset: Int): CPointer<UByte> = CPointer<UByte>(addr(offset))
    operator fun Array28UByte.minus(offset: Int): CPointer<UByte> = CPointer<UByte>(addr(-offset))
    operator fun Array16UByte.get(index: Int): UByte = lb(addr(index)).toUByte()
    operator fun Array16UByte.set(index: Int, value: UByte): Unit { sb(addr(index), (value).toByte()) }
    var Array16UByte.value get() = this[0]; set(value) { this[0] = value }
    inline fun Array16UByteAlloc(setItems: Array16UByte.() -> Unit): Array16UByte = Array16UByte(alloca_zero(Array16UByte.TOTAL_SIZE_BYTES).ptr).apply(setItems)
    operator fun Array16UByte.plus(offset: Int): CPointer<UByte> = CPointer<UByte>(addr(offset))
    operator fun Array16UByte.minus(offset: Int): CPointer<UByte> = CPointer<UByte>(addr(-offset))
    operator fun Array24UByte.get(index: Int): UByte = lb(addr(index)).toUByte()
    operator fun Array24UByte.set(index: Int, value: UByte): Unit { sb(addr(index), (value).toByte()) }
    var Array24UByte.value get() = this[0]; set(value) { this[0] = value }
    inline fun Array24UByteAlloc(setItems: Array24UByte.() -> Unit): Array24UByte = Array24UByte(alloca_zero(Array24UByte.TOTAL_SIZE_BYTES).ptr).apply(setItems)
    operator fun Array24UByte.plus(offset: Int): CPointer<UByte> = CPointer<UByte>(addr(offset))
    operator fun Array24UByte.minus(offset: Int): CPointer<UByte> = CPointer<UByte>(addr(-offset))
    operator fun Array10UByte.get(index: Int): UByte = lb(addr(index)).toUByte()
    operator fun Array10UByte.set(index: Int, value: UByte): Unit { sb(addr(index), (value).toByte()) }
    var Array10UByte.value get() = this[0]; set(value) { this[0] = value }
    inline fun Array10UByteAlloc(setItems: Array10UByte.() -> Unit): Array10UByte = Array10UByte(alloca_zero(Array10UByte.TOTAL_SIZE_BYTES).ptr).apply(setItems)
    operator fun Array10UByte.plus(offset: Int): CPointer<UByte> = CPointer<UByte>(addr(offset))
    operator fun Array10UByte.minus(offset: Int): CPointer<UByte> = CPointer<UByte>(addr(-offset))
    operator fun Array145Float.get(index: Int): Float = lwf(addr(index))
    operator fun Array145Float.set(index: Int, value: Float): Unit { swf(addr(index), (value)) }
    var Array145Float.value get() = this[0]; set(value) { this[0] = value }
    inline fun Array145FloatAlloc(setItems: Array145Float.() -> Unit): Array145Float = Array145Float(alloca_zero(Array145Float.TOTAL_SIZE_BYTES).ptr).apply(setItems)
    operator fun Array145Float.plus(offset: Int): CPointer<Float> = CPointer<Float>(addr(offset))
    operator fun Array145Float.minus(offset: Int): CPointer<Float> = CPointer<Float>(addr(-offset))
    operator fun Array32Short.get(index: Int): Short = lh(addr(index))
    operator fun Array32Short.set(index: Int, value: Short): Unit { sh(addr(index), value) }
    var Array32Short.value get() = this[0]; set(value) { this[0] = value }
    inline fun Array32ShortAlloc(setItems: Array32Short.() -> Unit): Array32Short = Array32Short(alloca_zero(Array32Short.TOTAL_SIZE_BYTES).ptr).apply(setItems)
    operator fun Array32Short.plus(offset: Int): CPointer<Short> = CPointer<Short>(addr(offset))
    operator fun Array32Short.minus(offset: Int): CPointer<Short> = CPointer<Short>(addr(-offset))
    operator fun Array3Int.get(index: Int): Int = lw(addr(index))
    operator fun Array3Int.set(index: Int, value: Int): Unit { sw(addr(index), value) }
    var Array3Int.value get() = this[0]; set(value) { this[0] = value }
    inline fun Array3IntAlloc(setItems: Array3Int.() -> Unit): Array3Int = Array3Int(alloca_zero(Array3Int.TOTAL_SIZE_BYTES).ptr).apply(setItems)
    operator fun Array3Int.plus(offset: Int): CPointer<Int> = CPointer<Int>(addr(offset))
    operator fun Array3Int.minus(offset: Int): CPointer<Int> = CPointer<Int>(addr(-offset))
    operator fun Array14Float.get(index: Int): Float = lwf(addr(index))
    operator fun Array14Float.set(index: Int, value: Float): Unit { swf(addr(index), (value)) }
    var Array14Float.value get() = this[0]; set(value) { this[0] = value }
    inline fun Array14FloatAlloc(setItems: Array14Float.() -> Unit): Array14Float = Array14Float(alloca_zero(Array14Float.TOTAL_SIZE_BYTES).ptr).apply(setItems)
    operator fun Array14Float.plus(offset: Int): CPointer<Float> = CPointer<Float>(addr(offset))
    operator fun Array14Float.minus(offset: Int): CPointer<Float> = CPointer<Float>(addr(-offset))
    operator fun Array2Array8Float.get(index: Int): Array8Float = Array8Float(addr(index))
    operator fun Array2Array8Float.set(index: Int, value: Array8Float): Unit { memcpy(CPointer(addr(index)), CPointer(value.ptr), Array2Array8Float.ELEMENT_SIZE_BYTES) }
    var Array2Array8Float.value get() = this[0]; set(value) { this[0] = value }
    inline fun Array2Array8FloatAlloc(setItems: Array2Array8Float.() -> Unit): Array2Array8Float = Array2Array8Float(alloca_zero(Array2Array8Float.TOTAL_SIZE_BYTES).ptr).apply(setItems)
    operator fun Array2Array8Float.plus(offset: Int): CPointer<Array8Float> = CPointer<Array8Float>(addr(offset))
    operator fun Array2Array8Float.minus(offset: Int): CPointer<Array8Float> = CPointer<Array8Float>(addr(-offset))
    operator fun Array8Float.get(index: Int): Float = lwf(addr(index))
    operator fun Array8Float.set(index: Int, value: Float): Unit { swf(addr(index), (value)) }
    var Array8Float.value get() = this[0]; set(value) { this[0] = value }
    inline fun Array8FloatAlloc(setItems: Array8Float.() -> Unit): Array8Float = Array8Float(alloca_zero(Array8Float.TOTAL_SIZE_BYTES).ptr).apply(setItems)
    operator fun Array8Float.plus(offset: Int): CPointer<Float> = CPointer<Float>(addr(offset))
    operator fun Array8Float.minus(offset: Int): CPointer<Float> = CPointer<Float>(addr(-offset))
    operator fun Array18Float.get(index: Int): Float = lwf(addr(index))
    operator fun Array18Float.set(index: Int, value: Float): Unit { swf(addr(index), (value)) }
    var Array18Float.value get() = this[0]; set(value) { this[0] = value }
    inline fun Array18FloatAlloc(setItems: Array18Float.() -> Unit): Array18Float = Array18Float(alloca_zero(Array18Float.TOTAL_SIZE_BYTES).ptr).apply(setItems)
    operator fun Array18Float.plus(offset: Int): CPointer<Float> = CPointer<Float>(addr(offset))
    operator fun Array18Float.minus(offset: Int): CPointer<Float> = CPointer<Float>(addr(-offset))
    operator fun Array9Float.get(index: Int): Float = lwf(addr(index))
    operator fun Array9Float.set(index: Int, value: Float): Unit { swf(addr(index), (value)) }
    var Array9Float.value get() = this[0]; set(value) { this[0] = value }
    inline fun Array9FloatAlloc(setItems: Array9Float.() -> Unit): Array9Float = Array9Float(alloca_zero(Array9Float.TOTAL_SIZE_BYTES).ptr).apply(setItems)
    operator fun Array9Float.plus(offset: Int): CPointer<Float> = CPointer<Float>(addr(offset))
    operator fun Array9Float.minus(offset: Int): CPointer<Float> = CPointer<Float>(addr(-offset))
    operator fun Array6Float.get(index: Int): Float = lwf(addr(index))
    operator fun Array6Float.set(index: Int, value: Float): Unit { swf(addr(index), (value)) }
    var Array6Float.value get() = this[0]; set(value) { this[0] = value }
    inline fun Array6FloatAlloc(setItems: Array6Float.() -> Unit): Array6Float = Array6Float(alloca_zero(Array6Float.TOTAL_SIZE_BYTES).ptr).apply(setItems)
    operator fun Array6Float.plus(offset: Int): CPointer<Float> = CPointer<Float>(addr(offset))
    operator fun Array6Float.minus(offset: Int): CPointer<Float> = CPointer<Float>(addr(-offset))
    operator fun Array3Float.get(index: Int): Float = lwf(addr(index))
    operator fun Array3Float.set(index: Int, value: Float): Unit { swf(addr(index), (value)) }
    var Array3Float.value get() = this[0]; set(value) { this[0] = value }
    inline fun Array3FloatAlloc(setItems: Array3Float.() -> Unit): Array3Float = Array3Float(alloca_zero(Array3Float.TOTAL_SIZE_BYTES).ptr).apply(setItems)
    operator fun Array3Float.plus(offset: Int): CPointer<Float> = CPointer<Float>(addr(offset))
    operator fun Array3Float.minus(offset: Int): CPointer<Float> = CPointer<Float>(addr(-offset))
    operator fun Array2Array18Float.get(index: Int): Array18Float = Array18Float(addr(index))
    operator fun Array2Array18Float.set(index: Int, value: Array18Float): Unit { memcpy(CPointer(addr(index)), CPointer(value.ptr), Array2Array18Float.ELEMENT_SIZE_BYTES) }
    var Array2Array18Float.value get() = this[0]; set(value) { this[0] = value }
    inline fun Array2Array18FloatAlloc(setItems: Array2Array18Float.() -> Unit): Array2Array18Float = Array2Array18Float(alloca_zero(Array2Array18Float.TOTAL_SIZE_BYTES).ptr).apply(setItems)
    operator fun Array2Array18Float.plus(offset: Int): CPointer<Array18Float> = CPointer<Array18Float>(addr(offset))
    operator fun Array2Array18Float.minus(offset: Int): CPointer<Array18Float> = CPointer<Array18Float>(addr(-offset))
    operator fun Array24Float.get(index: Int): Float = lwf(addr(index))
    operator fun Array24Float.set(index: Int, value: Float): Unit { swf(addr(index), (value)) }
    var Array24Float.value get() = this[0]; set(value) { this[0] = value }
    inline fun Array24FloatAlloc(setItems: Array24Float.() -> Unit): Array24Float = Array24Float(alloca_zero(Array24Float.TOTAL_SIZE_BYTES).ptr).apply(setItems)
    operator fun Array24Float.plus(offset: Int): CPointer<Float> = CPointer<Float>(addr(offset))
    operator fun Array24Float.minus(offset: Int): CPointer<Float> = CPointer<Float>(addr(-offset))
    operator fun Array4Array8Float.get(index: Int): Array8Float = Array8Float(addr(index))
    operator fun Array4Array8Float.set(index: Int, value: Array8Float): Unit { memcpy(CPointer(addr(index)), CPointer(value.ptr), Array4Array8Float.ELEMENT_SIZE_BYTES) }
    var Array4Array8Float.value get() = this[0]; set(value) { this[0] = value }
    inline fun Array4Array8FloatAlloc(setItems: Array4Array8Float.() -> Unit): Array4Array8Float = Array4Array8Float(alloca_zero(Array4Array8Float.TOTAL_SIZE_BYTES).ptr).apply(setItems)
    operator fun Array4Array8Float.plus(offset: Int): CPointer<Array8Float> = CPointer<Array8Float>(addr(offset))
    operator fun Array4Array8Float.minus(offset: Int): CPointer<Array8Float> = CPointer<Array8Float>(addr(-offset))
    operator fun Array1bs_t.get(index: Int): bs_t = bs_t(addr(index))
    operator fun Array1bs_t.set(index: Int, value: bs_t): Unit { bs_t(addr(index)).copyFrom(value) }
    var Array1bs_t.value get() = this[0]; set(value) { this[0] = value }
    inline fun Array1bs_tAlloc(setItems: Array1bs_t.() -> Unit): Array1bs_t = Array1bs_t(alloca_zero(Array1bs_t.TOTAL_SIZE_BYTES).ptr).apply(setItems)
    operator fun Array1bs_t.plus(offset: Int): CPointer<bs_t> = CPointer<bs_t>(addr(offset))
    operator fun Array1bs_t.minus(offset: Int): CPointer<bs_t> = CPointer<bs_t>(addr(-offset))
    operator fun Array1L12_scale_info.get(index: Int): L12_scale_info = L12_scale_info(addr(index))
    operator fun Array1L12_scale_info.set(index: Int, value: L12_scale_info): Unit { L12_scale_info(addr(index)).copyFrom(value) }
    var Array1L12_scale_info.value get() = this[0]; set(value) { this[0] = value }
    inline fun Array1L12_scale_infoAlloc(setItems: Array1L12_scale_info.() -> Unit): Array1L12_scale_info = Array1L12_scale_info(alloca_zero(Array1L12_scale_info.TOTAL_SIZE_BYTES).ptr).apply(setItems)
    operator fun Array1L12_scale_info.plus(offset: Int): CPointer<L12_scale_info> = CPointer<L12_scale_info>(addr(offset))
    operator fun Array1L12_scale_info.minus(offset: Int): CPointer<L12_scale_info> = CPointer<L12_scale_info>(addr(-offset))
}

//////////////////
// C STRUCTURES //
//////////////////

public inline/*!*/ class mp3dec_frame_info_t(val ptr: Int) : AbstractRuntime.IStruct {
    companion object : AbstractRuntime.IStructCompanion<mp3dec_frame_info_t>  {
        const val SIZE_BYTES = 20
        override val SIZE = SIZE_BYTES
        const val OFFSET_frame_bytes = 0
        const val OFFSET_channels = 4
        const val OFFSET_hz = 8
        const val OFFSET_layer = 12
        const val OFFSET_bitrate_kbps = 16
    }
}
public inline/*!*/ class mp3dec_t(val ptr: Int) : AbstractRuntime.IStruct {
    companion object : AbstractRuntime.IStructCompanion<mp3dec_t>  {
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
public inline/*!*/ class bs_t(val ptr: Int) : AbstractRuntime.IStruct {
    companion object : AbstractRuntime.IStructCompanion<bs_t>  {
        const val SIZE_BYTES = 12
        override val SIZE = SIZE_BYTES
        const val OFFSET_buf = 0
        const val OFFSET_pos = 4
        const val OFFSET_limit = 8
    }
}
public inline/*!*/ class L12_scale_info(val ptr: Int) : AbstractRuntime.IStruct {
    companion object : AbstractRuntime.IStructCompanion<L12_scale_info>  {
        const val SIZE_BYTES = 898
        override val SIZE = SIZE_BYTES
        const val OFFSET_scf = 0
        const val OFFSET_total_bands = 768
        const val OFFSET_stereo_bands = 769
        const val OFFSET_bitalloc = 770
        const val OFFSET_scfcod = 834
    }
}
public inline/*!*/ class L12_subband_alloc_t(val ptr: Int) : AbstractRuntime.IStruct {
    companion object : AbstractRuntime.IStructCompanion<L12_subband_alloc_t>  {
        const val SIZE_BYTES = 3
        override val SIZE = SIZE_BYTES
        const val OFFSET_tab_offset = 0
        const val OFFSET_code_tab_width = 1
        const val OFFSET_band_count = 2
    }
}
public inline/*!*/ class L3_gr_info_t(val ptr: Int) : AbstractRuntime.IStruct {
    companion object : AbstractRuntime.IStructCompanion<L3_gr_info_t>  {
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
public inline/*!*/ class mp3dec_scratch_t(val ptr: Int) : AbstractRuntime.IStruct {
    companion object : AbstractRuntime.IStructCompanion<mp3dec_scratch_t>  {
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
public inline/*!*/ class Array2Array288Float(val ptr: Int) {
    companion object {
        const val NUM_ELEMENTS = 2
        const val ELEMENT_SIZE_BYTES = 1152
        const val TOTAL_SIZE_BYTES = /*2304*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
    }
    fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
}
public inline/*!*/ class Array288Float(val ptr: Int) {
    companion object {
        const val NUM_ELEMENTS = 288
        const val ELEMENT_SIZE_BYTES = 4
        const val TOTAL_SIZE_BYTES = /*1152*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
    }
    fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
}
public inline/*!*/ class Array960Float(val ptr: Int) {
    companion object {
        const val NUM_ELEMENTS = 960
        const val ELEMENT_SIZE_BYTES = 4
        const val TOTAL_SIZE_BYTES = /*3840*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
    }
    fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
}
public inline/*!*/ class Array4UByte(val ptr: Int) {
    companion object {
        const val NUM_ELEMENTS = 4
        const val ELEMENT_SIZE_BYTES = 1
        const val TOTAL_SIZE_BYTES = /*4*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
    }
    fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
}
public inline/*!*/ class Array511UByte(val ptr: Int) {
    companion object {
        const val NUM_ELEMENTS = 511
        const val ELEMENT_SIZE_BYTES = 1
        const val TOTAL_SIZE_BYTES = /*511*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
    }
    fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
}
public inline/*!*/ class Array192Float(val ptr: Int) {
    companion object {
        const val NUM_ELEMENTS = 192
        const val ELEMENT_SIZE_BYTES = 4
        const val TOTAL_SIZE_BYTES = /*768*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
    }
    fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
}
public inline/*!*/ class Array64UByte(val ptr: Int) {
    companion object {
        const val NUM_ELEMENTS = 64
        const val ELEMENT_SIZE_BYTES = 1
        const val TOTAL_SIZE_BYTES = /*64*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
    }
    fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
}
public inline/*!*/ class Array3UByte(val ptr: Int) {
    companion object {
        const val NUM_ELEMENTS = 3
        const val ELEMENT_SIZE_BYTES = 1
        const val TOTAL_SIZE_BYTES = /*3*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
    }
    fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
}
public inline/*!*/ class Array2815UByte(val ptr: Int) {
    companion object {
        const val NUM_ELEMENTS = 2815
        const val ELEMENT_SIZE_BYTES = 1
        const val TOTAL_SIZE_BYTES = /*2815*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
    }
    fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
}
public inline/*!*/ class Array4L3_gr_info_t(val ptr: Int) {
    companion object {
        const val NUM_ELEMENTS = 4
        const val ELEMENT_SIZE_BYTES = 28
        const val TOTAL_SIZE_BYTES = /*112*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
    }
    fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
}
public inline/*!*/ class Array2Array576Float(val ptr: Int) {
    companion object {
        const val NUM_ELEMENTS = 2
        const val ELEMENT_SIZE_BYTES = 2304
        const val TOTAL_SIZE_BYTES = /*4608*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
    }
    fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
}
public inline/*!*/ class Array576Float(val ptr: Int) {
    companion object {
        const val NUM_ELEMENTS = 576
        const val ELEMENT_SIZE_BYTES = 4
        const val TOTAL_SIZE_BYTES = /*2304*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
    }
    fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
}
public inline/*!*/ class Array40Float(val ptr: Int) {
    companion object {
        const val NUM_ELEMENTS = 40
        const val ELEMENT_SIZE_BYTES = 4
        const val TOTAL_SIZE_BYTES = /*160*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
    }
    fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
}
public inline/*!*/ class Array33Array64Float(val ptr: Int) {
    companion object {
        const val NUM_ELEMENTS = 33
        const val ELEMENT_SIZE_BYTES = 256
        const val TOTAL_SIZE_BYTES = /*8448*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
    }
    fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
}
public inline/*!*/ class Array64Float(val ptr: Int) {
    companion object {
        const val NUM_ELEMENTS = 64
        const val ELEMENT_SIZE_BYTES = 4
        const val TOTAL_SIZE_BYTES = /*256*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
    }
    fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
}
public inline/*!*/ class Array2Array39UByte(val ptr: Int) {
    companion object {
        const val NUM_ELEMENTS = 2
        const val ELEMENT_SIZE_BYTES = 39
        const val TOTAL_SIZE_BYTES = /*78*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
    }
    fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
}
public inline/*!*/ class Array39UByte(val ptr: Int) {
    companion object {
        const val NUM_ELEMENTS = 39
        const val ELEMENT_SIZE_BYTES = 1
        const val TOTAL_SIZE_BYTES = /*39*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
    }
    fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
}
public inline/*!*/ class Array2Array3Array15UByte(val ptr: Int) {
    companion object {
        const val NUM_ELEMENTS = 2
        const val ELEMENT_SIZE_BYTES = 45
        const val TOTAL_SIZE_BYTES = /*90*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
    }
    fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
}
public inline/*!*/ class Array3Array15UByte(val ptr: Int) {
    companion object {
        const val NUM_ELEMENTS = 3
        const val ELEMENT_SIZE_BYTES = 15
        const val TOTAL_SIZE_BYTES = /*45*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
    }
    fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
}
public inline/*!*/ class Array15UByte(val ptr: Int) {
    companion object {
        const val NUM_ELEMENTS = 15
        const val ELEMENT_SIZE_BYTES = 1
        const val TOTAL_SIZE_BYTES = /*15*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
    }
    fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
}
public inline/*!*/ class Array3UInt(val ptr: Int) {
    companion object {
        const val NUM_ELEMENTS = 3
        const val ELEMENT_SIZE_BYTES = 4
        const val TOTAL_SIZE_BYTES = /*12*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
    }
    fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
}
public inline/*!*/ class Array54Float(val ptr: Int) {
    companion object {
        const val NUM_ELEMENTS = 54
        const val ELEMENT_SIZE_BYTES = 4
        const val TOTAL_SIZE_BYTES = /*216*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
    }
    fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
}
public inline/*!*/ class Array8Array23UByte(val ptr: Int) {
    companion object {
        const val NUM_ELEMENTS = 8
        const val ELEMENT_SIZE_BYTES = 23
        const val TOTAL_SIZE_BYTES = /*184*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
    }
    fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
}
public inline/*!*/ class Array23UByte(val ptr: Int) {
    companion object {
        const val NUM_ELEMENTS = 23
        const val ELEMENT_SIZE_BYTES = 1
        const val TOTAL_SIZE_BYTES = /*23*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
    }
    fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
}
public inline/*!*/ class Array8Array40UByte(val ptr: Int) {
    companion object {
        const val NUM_ELEMENTS = 8
        const val ELEMENT_SIZE_BYTES = 40
        const val TOTAL_SIZE_BYTES = /*320*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
    }
    fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
}
public inline/*!*/ class Array40UByte(val ptr: Int) {
    companion object {
        const val NUM_ELEMENTS = 40
        const val ELEMENT_SIZE_BYTES = 1
        const val TOTAL_SIZE_BYTES = /*40*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
    }
    fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
}
public inline/*!*/ class Array4Float(val ptr: Int) {
    companion object {
        const val NUM_ELEMENTS = 4
        const val ELEMENT_SIZE_BYTES = 4
        const val TOTAL_SIZE_BYTES = /*16*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
    }
    fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
}
public inline/*!*/ class Array3Array28UByte(val ptr: Int) {
    companion object {
        const val NUM_ELEMENTS = 3
        const val ELEMENT_SIZE_BYTES = 28
        const val TOTAL_SIZE_BYTES = /*84*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
    }
    fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
}
public inline/*!*/ class Array28UByte(val ptr: Int) {
    companion object {
        const val NUM_ELEMENTS = 28
        const val ELEMENT_SIZE_BYTES = 1
        const val TOTAL_SIZE_BYTES = /*28*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
    }
    fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
}
public inline/*!*/ class Array16UByte(val ptr: Int) {
    companion object {
        const val NUM_ELEMENTS = 16
        const val ELEMENT_SIZE_BYTES = 1
        const val TOTAL_SIZE_BYTES = /*16*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
    }
    fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
}
public inline/*!*/ class Array24UByte(val ptr: Int) {
    companion object {
        const val NUM_ELEMENTS = 24
        const val ELEMENT_SIZE_BYTES = 1
        const val TOTAL_SIZE_BYTES = /*24*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
    }
    fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
}
public inline/*!*/ class Array10UByte(val ptr: Int) {
    companion object {
        const val NUM_ELEMENTS = 10
        const val ELEMENT_SIZE_BYTES = 1
        const val TOTAL_SIZE_BYTES = /*10*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
    }
    fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
}
public inline/*!*/ class Array145Float(val ptr: Int) {
    companion object {
        const val NUM_ELEMENTS = 145
        const val ELEMENT_SIZE_BYTES = 4
        const val TOTAL_SIZE_BYTES = /*580*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
    }
    fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
}
public inline/*!*/ class Array32Short(val ptr: Int) {
    companion object {
        const val NUM_ELEMENTS = 32
        const val ELEMENT_SIZE_BYTES = 2
        const val TOTAL_SIZE_BYTES = /*64*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
    }
    fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
}
public inline/*!*/ class Array3Int(val ptr: Int) {
    companion object {
        const val NUM_ELEMENTS = 3
        const val ELEMENT_SIZE_BYTES = 4
        const val TOTAL_SIZE_BYTES = /*12*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
    }
    fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
}
public inline/*!*/ class Array14Float(val ptr: Int) {
    companion object {
        const val NUM_ELEMENTS = 14
        const val ELEMENT_SIZE_BYTES = 4
        const val TOTAL_SIZE_BYTES = /*56*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
    }
    fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
}
public inline/*!*/ class Array2Array8Float(val ptr: Int) {
    companion object {
        const val NUM_ELEMENTS = 2
        const val ELEMENT_SIZE_BYTES = 32
        const val TOTAL_SIZE_BYTES = /*64*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
    }
    fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
}
public inline/*!*/ class Array8Float(val ptr: Int) {
    companion object {
        const val NUM_ELEMENTS = 8
        const val ELEMENT_SIZE_BYTES = 4
        const val TOTAL_SIZE_BYTES = /*32*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
    }
    fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
}
public inline/*!*/ class Array18Float(val ptr: Int) {
    companion object {
        const val NUM_ELEMENTS = 18
        const val ELEMENT_SIZE_BYTES = 4
        const val TOTAL_SIZE_BYTES = /*72*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
    }
    fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
}
public inline/*!*/ class Array9Float(val ptr: Int) {
    companion object {
        const val NUM_ELEMENTS = 9
        const val ELEMENT_SIZE_BYTES = 4
        const val TOTAL_SIZE_BYTES = /*36*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
    }
    fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
}
public inline/*!*/ class Array6Float(val ptr: Int) {
    companion object {
        const val NUM_ELEMENTS = 6
        const val ELEMENT_SIZE_BYTES = 4
        const val TOTAL_SIZE_BYTES = /*24*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
    }
    fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
}
public inline/*!*/ class Array3Float(val ptr: Int) {
    companion object {
        const val NUM_ELEMENTS = 3
        const val ELEMENT_SIZE_BYTES = 4
        const val TOTAL_SIZE_BYTES = /*12*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
    }
    fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
}
public inline/*!*/ class Array2Array18Float(val ptr: Int) {
    companion object {
        const val NUM_ELEMENTS = 2
        const val ELEMENT_SIZE_BYTES = 72
        const val TOTAL_SIZE_BYTES = /*144*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
    }
    fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
}
public inline/*!*/ class Array24Float(val ptr: Int) {
    companion object {
        const val NUM_ELEMENTS = 24
        const val ELEMENT_SIZE_BYTES = 4
        const val TOTAL_SIZE_BYTES = /*96*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
    }
    fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
}
public inline/*!*/ class Array4Array8Float(val ptr: Int) {
    companion object {
        const val NUM_ELEMENTS = 4
        const val ELEMENT_SIZE_BYTES = 32
        const val TOTAL_SIZE_BYTES = /*128*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
    }
    fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
}
public inline/*!*/ class Array1bs_t(val ptr: Int) {
    companion object {
        const val NUM_ELEMENTS = 1
        const val ELEMENT_SIZE_BYTES = 12
        const val TOTAL_SIZE_BYTES = /*12*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
    }
    fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
}
public inline/*!*/ class Array1L12_scale_info(val ptr: Int) {
    companion object {
        const val NUM_ELEMENTS = 1
        const val ELEMENT_SIZE_BYTES = 898
        const val TOTAL_SIZE_BYTES = /*898*/ (NUM_ELEMENTS * ELEMENT_SIZE_BYTES)
    }
    fun addr(index: Int) = ptr + index * ELEMENT_SIZE_BYTES
}
