package com.kekulta.processor.models

import com.google.devtools.ksp.symbol.KSAnnotated

/**
 * Contains valid and invalid annotated symbols, invalid symbols should be returned by the processor for reprocessing in the next round
 *
 * @param valid valid symbols, are good to work with
 * @param invalid invalid symbols, should be reprocessed
 */
internal data class AnnotatedSymbols(
    val valid: List<KSAnnotated>,
    val invalid: List<KSAnnotated>,
)