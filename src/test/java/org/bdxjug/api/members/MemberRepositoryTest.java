package org.bdxjug.api.members;

import org.junit.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberRepositoryTest {

    @Test
    public void parseDate() {
        LocalDate localDate = MemberRepository.parseDate("28/11/2016");

        assertThat(localDate.getDayOfMonth()).isEqualTo(28);
        assertThat(localDate.getMonthValue()).isEqualTo(11);
        assertThat(localDate.getYear()).isEqualTo(2016);
    }


    @Test
    public void parseBadDate_should_not_fail() {
        LocalDate localDate = MemberRepository.parseDate("11/7/2016");

        assertThat(localDate).isEqualTo(LocalDate.ofEpochDay(0));
    }

}