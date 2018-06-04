package info.amoraes.n26.challenge.models;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder(toBuilder = true)
@EqualsAndHashCode
public class Summary {
  private double sum;
  private double avg;
  private double max;
  private double min;
  private int count;

  /**
   * Merge performs a merge operation between two summaries objects
   *
   * <p>merge is commutative and associative:
   *
   * <p>merge(a, merge(b, merge(c, d))) is equals to merge(b, merge(c, merge(d, b))) and so on
   *
   * <p>With this property, given a unordered set of unique summaries, a final summary can be
   * obtained regardless how the summaries are merged
   *
   * @param a
   * @param b
   * @return
   */
  public static Summary merge(Summary a, Summary b) {
    return Summary.builder()
        .max(Math.max(a.getMax(), b.getMax()))
        .min(Math.min(a.getMin(), b.getMin()))
        .count(a.getCount() + b.getCount())
        .sum(a.getSum() + b.getSum())
        .avg((a.getSum() + b.getSum()) / (a.getCount() + b.getCount()))
        .build();
  }

  public static class SummaryBuilder {

    /**
     * merge the given summary into the current one, updating the required fields as needed
     *
     * @param s
     * @return
     */
    public SummaryBuilder merge(Summary s) {
      this.max(maxOf(this.max, s.max));
      this.min(minOf(this.min, s.min));
      this.sum(this.sum + s.sum);
      this.count(this.count + s.count);
      this.avg(this.sum / this.count);

      return this;
    }

    /**
     * Add the given transaction to the current summary
     *
     * @param t
     * @return
     */
    public Summary from(Transaction t) {
      return this.max(t.getAmount())
          .min(t.getAmount())
          .count(1)
          .avg(t.getAmount())
          .sum(t.getAmount())
          .build();
    }

    private static double maxOf(double a, double b) {
      if (a < b) {
        return b;
      }
      return a;
    }

    private static double minOf(double a, double b) {
      if (a > b) {
        return b;
      }
      return a;
    }
  }
}
