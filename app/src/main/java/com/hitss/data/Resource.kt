package com.hitss.data

/**
 * Sealed class `Resource` represents different states of a resource that can be loaded, i.e. loading, success, and error.
 *
 * @param T generic type of the data being loaded.
 * @property data the data of the resource in case of success.
 * @property errorCode the error code in case of failure.
 */
sealed class Resource<T>(
    val data: T? = null,
    val errorCode: Int? = null
) {
    /**
     * Class `Success` represents the successful state of the resource with data.
     *
     * @property data the data of the resource.
     */
    class Success<T>(data: T) : Resource<T>(data)
    /**
     * Class `Loading` represents the loading state of the resource.
     *
     * @property data optional data for the loading state.
     */
    class Loading<T>(data: T? = null) : Resource<T>(data)
    /**
     * Class `DataError` represents the error state of the resource with error code.
     *
     * @property errorCode the error code of the resource.
     */
    class DataError<T>(errorCode: Int) : Resource<T>(null, errorCode)
    /**
     * Returns the string representation of the resource state.
     */
    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is DataError -> "Error[exception=$errorCode]"
            is Loading<T> -> "Loading"
        }
    }
}