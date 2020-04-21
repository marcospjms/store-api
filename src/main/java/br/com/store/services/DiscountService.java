    package br.com.store.services;

import br.com.store.model.Discount;
import br.com.store.model.PaymentType;
import br.com.store.model.Product;
import br.com.store.repositories.DiscountRepository;
import br.com.store.util.AbstractEntityUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


    @Service
public class DiscountService {

    @Autowired
    private DiscountRepository repository;

    public Discount save(Discount discount) {
        return this.repository.save(discount);
    }

    public boolean delete(Long id) {
        this.repository.deleteById(id);
        return true;
    }

    public List<Discount> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public List<Discount> findByQuery(Pageable pageable, String query) {
        query = AbstractEntityUtil.normalizeText(query);
        return repository.findAll(AbstractEntityUtil.createExample(new Discount(), query), pageable).getContent();
    }


    public Discount findById(Long id) {
        return repository.findById(id).get();
    }

    public Discount findValidByCode(String code) {
        List<Discount> discounts = this.repository.findByCodeAndDate(code, DateTime.now());
        if (discounts.size() > 1) {
            throw new RuntimeException("Código de cupom inválido: dois descontos localizados");
        }
        return discounts.get(0);
    }

    public boolean has(Long id) {
        return repository.findById(id).isPresent();
    }

    public double calcDiscount(List<Discount> discounts, List<Product> products, PaymentType paymentType) {
        List<Discount> bestDiscounts = this.getAppliedDiscounts(discounts, products, paymentType);
        return this.sumDiscounts(bestDiscounts, products, paymentType);
    }

    public List<Discount> getAppliedDiscounts(List<Discount> discounts, List<Product> products, PaymentType paymentType) {
        List<Discount> cumulativeDiscounts = getCumulativeDiscounts(discounts);
        List<Discount> nonCumulativeDiscounts = getNonCumulativeDiscounts(discounts);

        Double totalCumulativeDiscount = sumDiscounts(cumulativeDiscounts, products, paymentType);
        Double totalNonCumulativeDiscount = sumDiscounts(nonCumulativeDiscounts, products, paymentType);

        return totalCumulativeDiscount > totalNonCumulativeDiscount ? cumulativeDiscounts : nonCumulativeDiscounts;
    }

    public List<Discount> getCumulativeDiscounts(List<Discount> discounts) {
        return discounts.stream().filter(discount -> discount.isCumulative()).collect(Collectors.toList());
    }

    public List<Discount> getNonCumulativeDiscounts(List<Discount> discounts) {
        return discounts.stream().filter(discount -> !discount.isCumulative()).collect(Collectors.toList());
    }

    public double sumDiscounts(List<Discount> discounts, List<Product> products, PaymentType paymentType) {
        return discounts.stream().reduce(
                0.0,
                (subtotal, discount) -> subtotal + discount.calculate(products, paymentType),
                Double::sum
        );
    }
}
