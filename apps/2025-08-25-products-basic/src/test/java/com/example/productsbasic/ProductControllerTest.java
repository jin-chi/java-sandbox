package com.example.productsbasic;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
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

@WebMvcTest(ProductController.class)
@Import(GlobalExceptionHandler.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService service;

    @MockitoBean
    private ProductMapper mapper;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void get_findAll_200_ok() throws Exception {
        when(service.getAllProducts()).thenReturn(Arrays
                .asList(new ProductGetResponseDto(1L, "test product", BigDecimal.valueOf(1000), 10,
                        LocalDateTime.parse("2025-09-15T12:34:56.123456"))));
        mockMvc.perform(get("/products"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("test product"))
                .andExpect(jsonPath("$[0].price").value(BigDecimal.valueOf(1000)))
                .andExpect(jsonPath("$[0].stock").value(10))
                .andExpect(jsonPath("$[0].createdAt").value("2025-09-15T12:34:56.123456"));
    }

    @Test
    void get_findById_200_ok() throws Exception {
        when(service.getProductById(1L)).thenReturn(
                new ProductGetResponseDto(1L, "test product", BigDecimal.valueOf(1000), 10,
                        LocalDateTime.parse("2025-09-15T12:34:56.123456")));
        mockMvc.perform(get("/products/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("test product"))
                .andExpect(jsonPath("$.price").value(1000))
                .andExpect(jsonPath("$.stock").value(10))
                .andExpect(jsonPath("$.createdAt").value("2025-09-15T12:34:56.123456"));
    }

    @Test
    void get_findById_404_notFound() throws Exception {
        when(service.getProductById(999L)).thenThrow(new ResourceNotFoundException("ユーザーID: 999 が存在しません"));

        mockMvc.perform(get("/products/999"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail").value("ユーザーID: 999 が存在しません"))
                .andExpect(jsonPath("$.type").value("about:blank"))
                .andExpect(jsonPath("$.instance").value("/products/999"));
    }

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

    @Test
    void post_save_400_badRequest() throws Exception {
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
                            hasEntry("defaultMessage", "0以上の数値を指定してください"))
                )));
    }
}
