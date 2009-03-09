package org.jquantlib.pricingengines.vanilla.finitedifferences;

public class FDMultiPeriodEngine {

}


/*#ifndef quantlib_fd_multi_period_engine_hpp
#define quantlib_fd_multi_period_engine_hpp

#include <ql/pricingengines/vanilla/fdvanillaengine.hpp>
#include <ql/methods/finitedifferences/fdtypedefs.hpp>
#include <ql/instruments/oneassetoption.hpp>
#include <ql/event.hpp>

namespace QuantLib {

    class FDMultiPeriodEngine : public FDVanillaEngine {
      protected:
        FDMultiPeriodEngine(Size gridPoints=100, Size timeSteps=100,
                            bool timeDependent = false);
        mutable std::vector<boost::shared_ptr<Event> > events_;
        mutable std::vector<Time> stoppingTimes_;
        Size timeStepPerPeriod_;
        mutable SampledCurve prices_;
        virtual void setupArguments(
               const PricingEngine::arguments* args,
               const std::vector<boost::shared_ptr<Event> >& schedule) const {
            FDVanillaEngine::setupArguments(args);
            events_ = schedule;
            stoppingTimes_.clear();
            Size n = schedule.size();
            stoppingTimes_.reserve(n);
            for (Size i=0; i<n; ++i)
                stoppingTimes_.push_back(process_->time(events_[i]->date()));
        };
        virtual void setupArguments(const PricingEngine::arguments* a) const {
            FDVanillaEngine::setupArguments(a);
            const OneAssetOption::arguments *args =
                dynamic_cast<const OneAssetOption::arguments*>(a);
            QL_REQUIRE(args, "incorrect argument type");
            events_.clear();
            stoppingTimes_ = args->stoppingTimes;
        };

        virtual void calculate(PricingEngine::results*) const;
        mutable boost::shared_ptr<StandardStepCondition > stepCondition_;
        mutable boost::shared_ptr<StandardFiniteDifferenceModel> model_;
        virtual void executeIntermediateStep(Size step) const = 0;
        virtual void initializeStepCondition() const;
        virtual void initializeModel() const;
        Time getDividendTime(Size i) const {
            return stoppingTimes_[i];
        }
    };

}


#endif


#include <ql/pricingengines/vanilla/fdmultiperiodengine.hpp>

namespace QuantLib {
    FDMultiPeriodEngine::
    FDMultiPeriodEngine(Size gridPoints, Size timeSteps,
                        bool timeDependent)
    : FDVanillaEngine(gridPoints, timeSteps, timeDependent),
      timeStepPerPeriod_(timeSteps) {}

    void FDMultiPeriodEngine::calculate(PricingEngine::results* r) const {
        OneAssetOption::results *results =
            dynamic_cast<OneAssetOption::results *>(r);
        QL_REQUIRE(results, "incorrect argument type");
        Time beginDate, endDate;
        Size dateNumber = stoppingTimes_.size();
        bool lastDateIsResTime = false;
        Integer firstIndex = -1;
        Integer lastIndex = dateNumber - 1;
        bool firstDateIsZero = false;
        Time firstNonZeroDate = getResidualTime();

        Real dateTolerance = 1e-6;

        if (dateNumber > 0){
            QL_REQUIRE(getDividendTime(0) >= 0,
                       "first date (" << getDividendTime(0)
                       << ") cannot be negative");
            if(getDividendTime(0) < getResidualTime() * dateTolerance ){
                firstDateIsZero = true;
                firstIndex = 0;
                if(dateNumber >= 2)
                    firstNonZeroDate = getDividendTime(1);
            }

            if (std::fabs(getDividendTime(lastIndex) - getResidualTime())
                < dateTolerance) {
                lastDateIsResTime = true;
                lastIndex = Integer(dateNumber) - 2;
            }

            if (!firstDateIsZero)
                firstNonZeroDate = getDividendTime(0);

            if (dateNumber >= 2) {
                for (Size j = 1; j < dateNumber; j++)
                    QL_REQUIRE(getDividendTime(j-1) < getDividendTime(j),
                               "dates must be in increasing order: "
                               << getDividendTime(j-1)
                               << " is not strictly smaller than "
                               << getDividendTime(j));
            }
        }

        Time dt = getResidualTime()/(timeStepPerPeriod_*(dateNumber+1));

        // Ensure that dt is always smaller than the first non-zero date
        if (firstNonZeroDate <= dt)
            dt = firstNonZeroDate/2.0;

        setGridLimits();
        initializeInitialCondition();
        initializeOperator();
        initializeBoundaryConditions();
        initializeModel();
        initializeStepCondition();

        prices_ = intrinsicValues_;
        if(lastDateIsResTime)
            executeIntermediateStep(dateNumber - 1);

        Integer j = lastIndex;
        do{
            if (j == Integer(dateNumber) - 1)
                beginDate = getResidualTime();
            else
                beginDate = getDividendTime(j+1);

            if (j >= 0)
                endDate = getDividendTime(j);
            else
                endDate = dt;

            model_->rollback(prices_.values(),
                             beginDate, endDate,
                             timeStepPerPeriod_, *stepCondition_);
            if (j >= 0)
                executeIntermediateStep(j);
        } while (--j >= firstIndex);

        model_->rollback(prices_.values(), dt, 0, 1, *stepCondition_);

        if(firstDateIsZero)
            executeIntermediateStep(0);

        results->value = prices_.valueAtCenter();
        results->delta = prices_.firstDerivativeAtCenter();
        results->gamma = prices_.secondDerivativeAtCenter();
        results->additionalResults["priceCurve"] = prices_;
    }

    void FDMultiPeriodEngine::initializeStepCondition() const{
        stepCondition_ = boost::shared_ptr<StandardStepCondition>(
                                                  new NullCondition<Array>());
    }

    void FDMultiPeriodEngine::initializeModel() const{
        model_ = boost::shared_ptr<StandardFiniteDifferenceModel>(
           new StandardFiniteDifferenceModel(finiteDifferenceOperator_,BCs_));
    }

}





*/