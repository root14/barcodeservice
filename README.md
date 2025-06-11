# Barcode Service

This project provides a RESTful API for generating and reading barcodes. You can generate various types of barcodes as PNG images and read barcodes from uploaded image files.

## Features

-   **Generate Barcodes**: Create barcodes in various formats (e.g., QR_CODE, CODE_128).
-   **Customize Dimensions**: Specify the width and height of the generated barcode.
-   **Store Barcodes**: Optionally, store the generated barcode image and retrieve it later using a UUID.
-   **Read Barcodes**: Read barcode data from an uploaded image file (supports `multipart/form-data` and `application/json`).

## API Endpoints

### Barcode Generation

#### 1. Generate a Barcode

Generates a barcode image based on the provided type, data, and dimensions.

-   **Endpoint**: `GET /generate`
-   **Description**: Creates a barcode image and returns its metadata. If `store=true`, the barcode is saved and can be retrieved later.

**Request Parameters:**

| Parameter | Type    | Required | Default | Description                                            |
| :-------- | :------ | :------- | :------ | :----------------------------------------------------- |
| `type`    | String  | Yes      |         | The barcode format (e.g., `QR_CODE`, `CODE_128`).      |
| `data`    | String  | Yes      |         | The data to encode in the barcode.                     |
| `width`   | Integer | No       | 400     | The width of the barcode image in pixels.              |
| `height`  | Integer | No       | 400     | The height of the barcode image in pixels.             |
| `store`   | Boolean | No       | `false` | If `true`, the barcode will be stored in the database. |

**Example Request:**

```bash
curl "http://localhost:8080/generate?type=QR_CODE&data=HelloWorld&width=250&height=250&store=true"
```

**Example Response (Success):**

-   **Status**: `200 OK`
-   **Body**:
    ```json
    {
        "uuid": "a1b2c3d4-e5f6-7890-1234-567890abcdef",
        "barcode": "iVBORw0KGgoAAAANSUhEUgAAAPoAAAD6CAYAAACI7/YfAAAA...",
        "createdAt": "2023-10-27T10:00:00.000Z"
    }
    ```

#### 2. Retrieve a Stored Barcode

Retrieves a previously stored barcode image by its UUID.

-   **Endpoint**: `GET /getBarcode`
-   **Description**: Fetches a stored barcode image as a PNG file.

**Request Parameters:**

| Parameter | Type   | Required | Description                             |
| :-------- | :----- | :------- | :-------------------------------------- |
| `uuid`    | String | Yes      | The UUID of the barcode to retrieve. |

**Example Request:**

```bash
curl "http://localhost:8080/getBarcode?uuid=a1b2c3d4-e5f6-7890-1234-567890abcdef"
```

**Example Response (Success):**

-   **Status**: `200 OK`
-   **Content-Type**: `image/png`
-   **Body**: The raw PNG image data.

### Barcode Reading

#### 1. Read from an Image File

Reads a barcode from an uploaded image file.

-   **Endpoint**: `POST /read`
-   **Description**: Decodes a barcode from an image file sent as `multipart/form-data`.

**Request Parameters:**

| Parameter     | Type          | Required | Description                           |
| :------------ | :------------ | :------- | :------------------------------------ |
| `data`        | MultipartFile | Yes      | The image file containing the barcode. |
| `hint`        | Map           | No       | Optional decoding hints.              |

**Example Request:**

```bash
curl -X POST "http://localhost:8080/read" -F "data=@/path/to/your/barcode.png"
```

**Example Response (Success):**

-   **Status**: `200 OK`
-   **Body**:
    ```json
    {
        "timestamp": 1635336000000,
        "text": "HelloWorld",
        "format": "QR_CODE"
    }
    ```

#### 2. Read from JSON Data

Reads a barcode from base64-encoded or binary data provided in a JSON request.

-   **Endpoint**: `POST /read`
-   **Description**: Decodes a barcode from data sent in a JSON payload.

**Request Body:**

```json
{
    "data": "base64_or_binary_encoded_data"
}
```

**Request Parameters:**

| Parameter | Type | Required | Description              |
| :-------- | :--- | :------- | :----------------------- |
| `hint`    | Map  | No       | Optional decoding hints. |

**Example Request:**

```bash
curl -X POST "http://localhost:8080/read" \
-H "Content-Type: application/json" \
-d '{"data": "iVBORw0KGgoAAAANSUhEUgAAAPoAAAD6CAYAAACI7/YfAAAA..."}'
```

**Example Response (Success):**

-   **Status**: `200 OK`
-   **Body**:
    ```json
    {
        "timestamp": 1635336000000,
        "text": "HelloWorld",
        "format": "QR_CODE"
    }
    ```

## Error Handling

-   **`400 Bad Request`**: Returned if required parameters are missing or invalid.
-   **`404 Not Found`**: Returned if a requested barcode is not found.
-   **`500 Internal Server Error`**: Returned for any unexpected errors during processing.

## Testing

The project includes a comprehensive suite of unit and integration tests to ensure code quality and reliability. Tests are written using **JUnit** and **MockMVC** to validate the behavior of the controllers and services, ensuring that all API endpoints function as expected.

## Documentation

The source code is thoroughly documented using **Javadoc** comments. These comments provide detailed explanations for all classes, methods, and parameters throughout the project.

A browsable HTML version of the documentation can be generated from the source code by using the included Javadoc plugin.
```
