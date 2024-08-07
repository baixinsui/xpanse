package org.eclipse.xpanse.modules.models.response;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;

class ResponseTest {

    private final ResultType resultType = ResultType.ACCESS_DENIED;
    private final List<String> details = List.of(ResultType.ACCESS_DENIED.toValue());
    private final Boolean success = false;
    private Response test;

    @BeforeEach
    void setUp() {
        test = new Response();
        test.setResultType(resultType);
        test.setDetails(details);
        test.setSuccess(success);
    }

    @Test
    void testErrorResponse() {
        // Run the test
        final Response result = Response.errorResponse(resultType, details);
        assertThat(result.getResultType()).isEqualTo(resultType);
        assertThat(result.getDetails()).isEqualTo(details);
        assertThat(result.getSuccess()).isFalse();
    }

    @Test
    void testGetters() {
        assertThat(test.getResultType()).isEqualTo(resultType);
        assertThat(test.getDetails()).isEqualTo(details);
        assertThat(test.getSuccess()).isEqualTo(success);
    }

    @Test
    void testEqualsAndHashCode() {
        Object o = new Object();
        assertThat(test.canEqual(o)).isFalse();
        assertThat(test.equals(o)).isFalse();
        assertThat(test.hashCode()).isNotEqualTo(o.hashCode());

        Response test2 = new Response();
        assertThat(test.canEqual(test2)).isTrue();
        assertThat(test.equals(test2)).isFalse();
        assertThat(test.hashCode()).isNotEqualTo(test2.hashCode());

        BeanUtils.copyProperties(test, test2);
        assertThat(test.canEqual(test2)).isTrue();
        assertThat(test.equals(test2)).isTrue();
        assertThat(test.hashCode()).isEqualTo(test2.hashCode());
    }


    @Test
    void testToString() {
        String result =
                "Response(resultType=ACCESS_DENIED, details=[Access Denied], success=false)";
        assertThat(test.toString()).isEqualTo(result);
    }
}
