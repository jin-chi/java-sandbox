package com.example.productsbasic;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.json.JsonCompareMode;
import org.springframework.test.web.servlet.MockMvc;

import com.example.productsbasic.controller.ProductController;
import com.example.productsbasic.dto.ProductCreateUpdateResponseDto;
import com.example.productsbasic.dto.ProductGetResponseDto;
import com.example.productsbasic.dto.ProductRequestDto;
import com.example.productsbasic.entity.Product;
import com.example.productsbasic.exception.GlobalExceptionHandler;
import com.example.productsbasic.exception.ResourceNotFoundException;
import com.example.productsbasic.mapper.ProductMapper;
import com.example.productsbasic.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@WebMvcTest(ProductController.class)
@Import(GlobalExceptionHandler.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService service;

    @MockitoBean
    private ProductMapper mapper;

    private ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    // ----- GET ----- //
    @Test
    void get_findAll_200_ok() throws Exception {
        List<ProductGetResponseDto> responseDtoList = Arrays
                .asList(new ProductGetResponseDto(1L, "test product", BigDecimal.valueOf(1000), 10,
                        LocalDateTime.parse("2025-09-15T12:34:56.123456")));
        String responseDtoJson = objectMapper.writeValueAsString(responseDtoList);

        when(service.getAllProducts()).thenReturn(responseDtoList);
        mockMvc.perform(get("/products"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(content().json(responseDtoJson, JsonCompareMode.STRICT));
    }

    @Test
    void get_findById_200_ok() throws Exception {
        ProductGetResponseDto responseDto = new ProductGetResponseDto(1L, "test product", BigDecimal.valueOf(1000), 10,
                LocalDateTime.parse("2025-09-15T12:34:56.123456"));
        String responseDtoJson = objectMapper.writeValueAsString(responseDto);

        when(service.getProductById(1L)).thenReturn(responseDto);
        mockMvc.perform(get("/products/{id}", "1"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(responseDtoJson, JsonCompareMode.STRICT));
    }

    @Test // MethodArgumentTypeMismatchException
    void getfindById_400_badRequest() throws Exception {
        mockMvc.perform(get("/products/{id}", "abc"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value(
                        "Method parameter 'id': Failed to convert value of type 'java.lang.String' to required type 'java.lang.Long'; For input string: \"abc\""))
                .andExpect(jsonPath("$.type").value("about:blank"))
                .andExpect(jsonPath("$.instance").value("/products/abc"))
                .andExpect(jsonPath("$.title").value("Bad Request"));
    }

    @Test // ResourceNotFoundException
    void get_findById_404_notFound() throws Exception {
        when(service.getProductById(999L)).thenThrow(new ResourceNotFoundException("ユーザーID: 999 が存在しません"));

        mockMvc.perform(get("/products/{id}", "999"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail").value("ユーザーID: 999 が存在しません"))
                .andExpect(jsonPath("$.type").value("about:blank"))
                .andExpect(jsonPath("$.instance").value("/products/999"))
                .andExpect(jsonPath("$.title").value("Not Found"));
    }

    @Test // Other Exception
    void get_findAll_500_internalServerError() throws Exception {
        when(service.getAllProducts()).thenThrow(new RuntimeException("予期せぬエラーが発生しました"));

        mockMvc.perform(get("/products"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.detail").value("予期せぬエラーが発生しました"))
                .andExpect(jsonPath("$.type").value("about:blank"))
                .andExpect(jsonPath("$.instance").value("/products"))
                .andExpect(jsonPath("$.title").value("Internal Server Error"))
                .andExpect(jsonPath("$.exceptionName").value("java.lang.RuntimeException"));
    }

    @Test // Other Exception
    void get_findById_500_internalServerError() throws Exception {
        when(service.getProductById(1L)).thenThrow(new RuntimeException("予期せぬエラーが発生しました"));

        mockMvc.perform(get("/products/{id}", "1"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.detail").value("予期せぬエラーが発生しました"))
                .andExpect(jsonPath("$.type").value("about:blank"))
                .andExpect(jsonPath("$.instance").value("/products/1"))
                .andExpect(jsonPath("$.title").value("Internal Server Error"))
                .andExpect(jsonPath("$.exceptionName").value("java.lang.RuntimeException"));
    }

    // ----- POST ----- //

    @Test
    void post_save_201_ok() throws Exception {

        Product product = new Product("test product", BigDecimal.valueOf(1000), 10);
        product.setId(1L);
        product.setCreatedAt(LocalDateTime.parse("2025-09-15T12:34:56.123456"));
        ProductRequestDto requestDto = new ProductRequestDto("test product", BigDecimal.valueOf(1000), 10);
        ProductCreateUpdateResponseDto responseDto = new ProductCreateUpdateResponseDto("test product",
                new BigDecimal(1000), 10);

        String requestDtoJson = objectMapper.writeValueAsString(requestDto);
        String responseDtoJson = objectMapper.writeValueAsString(responseDto);

        when(service.createProduct(any(ProductRequestDto.class))).thenReturn(product);
        when(mapper.toCreateUpdateResponseDto(product)).thenReturn(responseDto);

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestDtoJson))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/products/1"))
                .andExpect(content().json(responseDtoJson, JsonCompareMode.STRICT));
    }

    @Test // HttpMessaegNotReadableException
    void post_badJson_400_badRequest() throws Exception {
        String requestDtoBadJson = """
                {
                    "name": "test product",
                    "price": 1000
                    "stock": 10
                }
                """;

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestDtoBadJson))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value(
                        "JSON parse error: Unexpected character ('\"' (code 34)): was expecting comma to separate Object entries"))
                .andExpect(jsonPath("$.type").value("about:blank"))
                .andExpect(jsonPath("$.instance").value("/products"))
                .andExpect(jsonPath("$.title").value("Bad Request"));
    }

    @Test // MethodArbumentNotValidException
    void post_requestBody_validationFaild_400_badRequest() throws Exception {
        ProductRequestDto requestDto = new ProductRequestDto("", BigDecimal.valueOf(-1), -10);
        String requestDtoJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestDtoJson))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value("Validation failed to request body."))
                .andExpect(jsonPath("$.type").value("about:blank"))
                .andExpect(jsonPath("$.instance").value("/products"))
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.errors", hasSize(3)))
                .andExpect(jsonPath("$.errors", containsInAnyOrder(
                        allOf(
                                hasEntry("field", "name"),
                                hasEntry("defaultMessage", "must not be blank")),
                        allOf(
                                hasEntry("field", "price"),
                                hasEntry("defaultMessage", "must be greater than or equal to 0")),
                        allOf(
                                hasEntry("field", "stock"),
                                hasEntry("defaultMessage", "0以上の数値を指定してください")))));
    }

    @Test // HttpRequestMethodNotSupportedException
    void post_bad_405_badRequest() throws Exception {
        ProductRequestDto requestDto = new ProductRequestDto("test product", BigDecimal.valueOf(1000), 10);
        mockMvc.perform(post("/products/{id}", '1')
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value("405"))
                .andExpect(jsonPath("$.detail").value("Request method 'POST' is not supported"))
                .andExpect(jsonPath("$.type").value("about:blank"))
                .andExpect(jsonPath("$.instance").value("/products/1"))
                .andExpect(jsonPath("$.title").value("Method Not Allowed"));
    }

    @Test // Other Exception
    void post_save_500_internalServerError() throws Exception {
        ProductRequestDto requestDto = new ProductRequestDto("test product", BigDecimal.valueOf(1000), 1000);
        when(service.createProduct(any(ProductRequestDto.class))).thenThrow(new RuntimeException("予期せぬエラーが発生しました"));

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.detail").value("予期せぬエラーが発生しました"))
                .andExpect(jsonPath("$.type").value("about:blank"))
                .andExpect(jsonPath("$.instance").value("/products"))
                .andExpect(jsonPath("$.title").value("Internal Server Error"));
    }

    // ----- PUT ----- //

    @Test
    void put_save_200_ok() throws Exception {
        ProductRequestDto requestDto = new ProductRequestDto("test product", BigDecimal.valueOf(1000), 10);
        ProductCreateUpdateResponseDto responseDto = new ProductCreateUpdateResponseDto("test product",
                BigDecimal.valueOf(1000), 10);

        String requestDtoJson = objectMapper.writeValueAsString(requestDto);
        String responseDtoJson = objectMapper.writeValueAsString(responseDto);

        when(service.updateProduct(eq(1L), any(ProductRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(put("/products/{id}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestDtoJson))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(responseDtoJson, JsonCompareMode.STRICT));
    }

    @Test // HttpMessageNotReadableException
    void put_badJson_400_badRequest() throws Exception {
        String requestDtoBadJson = """
                {
                    "name": "test product",
                    "price": 1000
                    "stock": 10
                }
                """;

        mockMvc.perform(put("/products/{id}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestDtoBadJson))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value(
                        "JSON parse error: Unexpected character ('\"' (code 34)): was expecting comma to separate Object entries"))
                .andExpect(jsonPath("$.type").value("about:blank"))
                .andExpect(jsonPath("$.instance").value("/products/1"))
                .andExpect(jsonPath("$.title").value("Bad Request"));
    }

    @Test // MethodArgumentTypeMismatchException
    void put_badPathParam_400_badRequest() throws Exception {
        ProductRequestDto requestDto = new ProductRequestDto("test product", BigDecimal.valueOf(1000), 10);
        String requestDtoJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(put("/products/{id}", "abc")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestDtoJson))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value(
                        "Method parameter 'id': Failed to convert value of type 'java.lang.String' to required type 'java.lang.Long'; For input string: \"abc\""))
                .andExpect(jsonPath("$.type").value("about:blank"))
                .andExpect(jsonPath("$.instance").value("/products/abc"))
                .andExpect(jsonPath("$.title").value("Bad Request"));
    }

    @Test // MethodArgumentNotValidException
    void put_requestBody_validationFaild_400_badRequest() throws Exception {
        ProductRequestDto requestDto = new ProductRequestDto("", BigDecimal.valueOf(-1), -10);
        String requestDtoJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(put("/products/{id}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestDtoJson))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value("Validation failed to request body."))
                .andExpect(jsonPath("$.type").value("about:blank"))
                .andExpect(jsonPath("$.instance").value("/products/1"))
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.errors", hasSize(3)))
                .andExpect(jsonPath("$.errors", containsInAnyOrder(
                        allOf(
                                hasEntry("field", "name"),
                                hasEntry("defaultMessage", "must not be blank")),
                        allOf(
                                hasEntry("field", "price"),
                                hasEntry("defaultMessage", "must be greater than or equal to 0")),
                        allOf(
                                hasEntry("field", "stock"),
                                hasEntry("defaultMessage", "0以上の数値を指定してください")))));
    }

    @Test // NotResourceFoundException
    void put_badUri_404_notFound() throws Exception {
        ProductRequestDto requestDto = new ProductRequestDto("test product", BigDecimal.valueOf(1000), 10);
        String requestDtoJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(put("/not-exists-uri/{id}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestDtoJson))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail").value("No static resource not-exists-uri/1."))
                .andExpect(jsonPath("$.type").value("about:blank"))
                .andExpect(jsonPath("$.instance").value("/not-exists-uri/1"))
                .andExpect(jsonPath("$.title").value("Not Found"));
    }

    @Test // ResourceNotFoundException
    void put_resourceNotFound_404_notFound() throws Exception {
        ProductRequestDto requestDto = new ProductRequestDto("test product", BigDecimal.valueOf(1000), 10);
        String requestDtoJson = objectMapper.writeValueAsString(requestDto);

        when(service.updateProduct(eq(999L), any(ProductRequestDto.class)))
                .thenThrow(new ResourceNotFoundException("ユーザーID: 999 が存在しません"));

        mockMvc.perform(put("/products/{id}", "999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestDtoJson))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail").value("ユーザーID: 999 が存在しません"))
                .andExpect(jsonPath("$.type").value("about:blank"))
                .andExpect(jsonPath("$.instance").value("/products/999"))
                .andExpect(jsonPath("$.title").value("Not Found"));
    }

    @Test // HttpRequestNotSupportedException
    void put_findAll_405_methodNotAllowed() throws Exception {
        mockMvc.perform(put("/products"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.detail").value("Request method 'PUT' is not supported"))
                .andExpect(jsonPath("$.type").value("about:blank"))
                .andExpect(jsonPath("$.instance").value("/products"))
                .andExpect(jsonPath("$.title").value("Method Not Allowed"));
    }

    @Test // DataIntegrityViolationException
    void put_save_409_conflict() throws Exception {
        ProductRequestDto requestDto = new ProductRequestDto("Conflict Product", BigDecimal.valueOf(1000), 10);
        String requestDtoJson = objectMapper.writeValueAsString(requestDto);

        when(service.updateProduct(1L, requestDto))
                .thenThrow(new DataIntegrityViolationException("Unique index or primary key violation"));

        mockMvc.perform(put("/products/{id}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestDtoJson))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.detail").value(
                        "org.springframework.dao.DataIntegrityViolationException: Unique index or primary key violation"))
                .andExpect(jsonPath("$.type").value("about:blank"))
                .andExpect(jsonPath("$.instance").value("/products/1"))
                .andExpect(jsonPath("$.title").value("Conflict"))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors", containsInAnyOrder(
                        allOf(hasEntry("errorMessage", "同じ商品名は登録できません")))));
    }

    @Test // Other Exception
    void put_save_500_internalServerError() throws Exception {
        ProductRequestDto requestDto = new ProductRequestDto("test product", BigDecimal.valueOf(1000), 10);
        when(service.updateProduct(eq(1L), any(ProductRequestDto.class)))
                .thenThrow(new RuntimeException("予期せぬエラーが発生しました"));

        mockMvc.perform(put("/products/{id}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.detail").value("予期せぬエラーが発生しました"))
                .andExpect(jsonPath("$.type").value("about:blank"))
                .andExpect(jsonPath("$.instance").value("/products/1"))
                .andExpect(jsonPath("$.title").value("Internal Server Error"));
    }

    // ----- DELETE ----- //

    @Test
    void delete_deleteById_204_noContent() throws Exception {
        when(service.deleteProduct(1L)).thenReturn(true);

        mockMvc.perform(delete("/products/{id}", "1"))
                .andDo(print())
                .andExpect(header().doesNotExist("Content-Type"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }

    @Test // MethodArgumentTypeMismatchException
    void delete_badPathParam_400_badRequest() throws Exception {
        mockMvc.perform(delete("/products/{id}", "abc"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value(
                        "Method parameter 'id': Failed to convert value of type 'java.lang.String' to required type 'java.lang.Long'; For input string: \"abc\""))
                .andExpect(jsonPath("$.type").value("about:blank"))
                .andExpect(jsonPath("$.instance").value("/products/abc"))
                .andExpect(jsonPath("$.title").value("Bad Request"));
    }

    @Test // NotResourceFoundException
    void delete_badUri_400_badRequest() throws Exception {
        mockMvc.perform(delete("/not-exists-uri"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail").value("No static resource not-exists-uri."))
                .andExpect(jsonPath("$.type").value("about:blank"))
                .andExpect(jsonPath("$.instance").value("/not-exists-uri"))
                .andExpect(jsonPath("$.title").value("Not Found"));
    }

    @Test // ResourceNotFoundException
    void delete_resourceNotFound_404_notFound() throws Exception {
        when(service.deleteProduct(999L)).thenReturn(false);

        mockMvc.perform(delete("/products/{id}", "999"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail").value("ユーザーID: 999 が存在しません"))
                .andExpect(jsonPath("$.type").value("about:blank"))
                .andExpect(jsonPath("$.instance").value("/products/999"))
                .andExpect(jsonPath("$.title").value("Not Found"));
    }

    @Test // HttpRequestMethodNotSupportedException
    void delete_findAll_405_methodNotAllowed() throws Exception {
        mockMvc.perform(delete("/products"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value(405))
                .andExpect(jsonPath("$.detail").value("Request method 'DELETE' is not supported"))
                .andExpect(jsonPath("$.type").value("about:blank"))
                .andExpect(jsonPath("$.instance").value("/products"))
                .andExpect(jsonPath("$.title").value("Method Not Allowed"));
    }

    @Test // Other Exception
    void delete_interNalServerError_500() throws Exception {
        when(service.deleteProduct(1L)).thenThrow(new RuntimeException("予期せぬエラーが発生しました"));

        mockMvc.perform(delete("/products/{id}", "1"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.detail").value("予期せぬエラーが発生しました"))
                .andExpect(jsonPath("$.type").value("about:blank"))
                .andExpect(jsonPath("$.instance").value("/products/1"))
                .andExpect(jsonPath("$.title").value("Internal Server Error"));
    }
}
